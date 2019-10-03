package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Kafka streams for generating the warehouse order from the aggregated warehouse order lines
 */
@Component
public class WarehouseOrderGeneratorStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderGeneratorStream.class);

    /** The name of the warehouse order lines Kafka topic (input KStream) */
    private final String newWarehouseOrderLinesTopic;

    /** The name of the warehouse order Kafka topic (output KStream) */
    private final String newWarehouseOrdersTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for the input warehouse order line avro value */
    private final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde;

    /** Serde for the output warehouse order avro value */
    private final Serde<WarehouseOrder> warehouseOrderValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl           the URL of the schema registry
     * @param newWarehouseOrderLinesTopic the name of the warehouse order lines Kafka topic (input KStream)
     * @param newWarehouseOrdersTopic     the name of the warehouse order Kafka topic (output KStream)
     */
    @Autowired
    public WarehouseOrderGeneratorStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.warehouse-order-lines-new}") String newWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-orders-new}") String newWarehouseOrdersTopic
    ) {
        super(schemaRegistryUrl);

        this.newWarehouseOrderLinesTopic = newWarehouseOrderLinesTopic;
        this.newWarehouseOrdersTopic = newWarehouseOrdersTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        this.warehouseOrderValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient        the schema registry client (for testing)
     * @param schemaRegistryUrl           the URL of the schema registry
     * @param newWarehouseOrderLinesTopic the name of the warehouse order lines Kafka topic (input KStream)
     * @param newWarehouseOrdersTopic     the name of the warehouse order Kafka topic (output KStream)
     */
    public WarehouseOrderGeneratorStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String newWarehouseOrderLinesTopic,
            String newWarehouseOrdersTopic
    ) {
        super(schemaRegistryUrl);

        this.newWarehouseOrderLinesTopic = newWarehouseOrderLinesTopic;
        this.newWarehouseOrdersTopic = newWarehouseOrdersTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        this.stringKeyAvroSerde.configure(serdeConfig, true);
        this.warehouseOrderLineValueAvroSerde.configure(serdeConfig, false);
        this.warehouseOrderValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - The groupBy operation will group all the warehouse order lines by a new key
     *
     *   The result is a KGrouped stream which will be used to aggregate the warehouse order lines in a warehouse order.
     *
     *   The new key will be composed by the country code and the date (one per day)
     *
     *   Key format: CC-YYYY-MM-DD-PPPPPP, where:
     *   - CC - The country code.
     *   - YYYY-MM-DD - The date.
     *
     * - The aggregate operation will add all the warehouse order lines to an array in the warehouse order.
     *
     *   So there will be a warehouse order per country and day with all the lines.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("warehouseOrdersGeneratorStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("warehouseOrdersGeneratorStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, WarehouseOrderLine> warehouseOrderLinesStream = builder.stream(
                newWarehouseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KGroupedStream<String, WarehouseOrderLine> warehouseOrderLinesGroupedStream = warehouseOrderLinesStream
                .groupBy(
                        (String poLineAggregationKey, WarehouseOrderLine poLine) -> {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date datetime = new Date(poLine.getDate());
                            return poLine.getCountry() + "-" + df.format(datetime);
                        },
                        Serialized.with(
                                stringKeyAvroSerde,
                                warehouseOrderLineValueAvroSerde
                        )
                );

        KStream<String, WarehouseOrder> warehouseOrderStream = warehouseOrderLinesGroupedStream
                .aggregate(
                        WarehouseOrder::new,
                        (String aggregationKey, WarehouseOrderLine newLine, WarehouseOrder aggregatedWO) -> {

                            String uuid = aggregatedWO.getUuid();
                            if (null == uuid) {
                                uuid = UUID.randomUUID().toString();
                            }

                            List<WarehouseOrderLineCondensed> newLines = new ArrayList<>();
                            if (null != aggregatedWO.getLines()) {
                                for (WarehouseOrderLineCondensed oldLine : aggregatedWO.getLines()) {
                                    if (!oldLine.getProductUuid().equals(newLine.getProductUuid())) {
                                        newLines.add(WarehouseOrderLineCondensed.newBuilder(oldLine).build());
                                    }
                                }
                            }

                            newLines.add(
                                    WarehouseOrderLineCondensed
                                            .newBuilder()
                                            .setUuid(newLine.getUuid())
                                            .setProductUuid(newLine.getProductUuid())
                                            .setProductLegacyId(newLine.getProductLegacyId())
                                            .setProductName(newLine.getProductName())
                                            .setProductBarCode(newLine.getProductBarCode())
                                            .setQuantity(newLine.getQuantity())
                                            .build()
                            );

                            WarehouseOrder result = WarehouseOrder
                                    .newBuilder(aggregatedWO)
                                    .setUuid(uuid)
                                    .setAggregationKey(aggregationKey)
                                    .setCountry(newLine.getCountry())
                                    .setDate(newLine.getDate())
                                    .setLines(newLines)
                                    .build();

                            LOGGER.info(">>> Stream - Warehouse order uuid={} aggregation-key={} - Aggregating warehouse order line uuid={}...", uuid, aggregationKey, newLine.getUuid());

                            return result;
                        },
                        Materialized.with(
                                stringKeyAvroSerde,
                                warehouseOrderValueAvroSerde
                        )
                )
                .toStream();

        warehouseOrderStream.to(
                newWarehouseOrdersTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderValueAvroSerde)
        );

        return builder;
    }
}
