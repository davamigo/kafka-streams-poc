package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Kafka streams for generating the warehouse order lines from the purchase order lines.
 */
@Component
public class WarehouseOrderLineGeneratorStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderLineGeneratorStream.class);

    /** The name of the purchase order lines Kafka topic (input KStream) */
    private final String purchaseOrderLinesTopic;

    /** The name of the warehouse3 order lines Kafka topic (output KStream) */
    private final String warehouseOrderLinesTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for encoding/decoding a purchase order line to/from avro */
    private final Serde<PurchaseOrderLine> purchaseOrderLineValueAvroSerde;

    /** Serde for encoding/decoding a warehouse order line to/from avro */
    private final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl        the URL of the schema registry
     * @param purchaseOrderLinesTopic  the name of the purchase order lines Kafka topic (input KStream)
     * @param warehouseOrderLinesTopic the name of the warehouse3 order lines Kafka topic (output KStream)
     */
    @Autowired
    public WarehouseOrderLineGeneratorStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.purchase-order-lines-aggregated}") String purchaseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-generated}") String warehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.purchaseOrderLinesTopic = purchaseOrderLinesTopic;
        this.warehouseOrderLinesTopic = warehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient     the schema registry client (for testing)
     * @param schemaRegistryUrl        the URL of the schema registry
     * @param purchaseOrderLinesTopic  the name of the purchase order lines Kafka topic (input KStream)
     * @param warehouseOrderLinesTopic the name of the warehouse3 order lines Kafka topic (output KStream)
     */
    public WarehouseOrderLineGeneratorStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String purchaseOrderLinesTopic,
            String warehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.purchaseOrderLinesTopic = purchaseOrderLinesTopic;
        this.warehouseOrderLinesTopic = warehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        stringKeyAvroSerde.configure(serdeConfig, true);
        purchaseOrderLineValueAvroSerde.configure(serdeConfig, false);
        warehouseOrderLineValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - This kafka streams will generate a warehouse order line from a purchase order line. It could be a map
     *   operation, but the source stream comes from a previous aggregation operation (purchase order lines), so it
     *   can contain several messages with the same key. We have to join all the messages. The last message will
     *   contain the right data.
     *
     * - The groupBy + aggregate operations will generate the warehouse order line, ensuring there is only one UUID for
     *   each warehouse order line.
     *
     * - The seletKey operation will change the key for the output stream.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("warehouseOrderLineGeneratorStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("warehouseOrderLineGeneratorStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, PurchaseOrderLine> purchaseOrderLinesStream = builder.stream(
                purchaseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesStrem = purchaseOrderLinesStream
                .groupBy(
                        (String purchaseOrderLineUuid, PurchaseOrderLine purchaseOrderLine) -> purchaseOrderLine.getAggregationKey(),
                        Serialized.with(
                                stringKeyAvroSerde,
                                purchaseOrderLineValueAvroSerde
                        )
                )
                .aggregate(
                        WarehouseOrderLine::new,
                        (String aggregationKey, PurchaseOrderLine newPurchaseOrderLine, WarehouseOrderLine aggregatedWarehouseOrderLine) -> {

                            String uuid = aggregatedWarehouseOrderLine.getUuid();
                            if (null == uuid) {
                                uuid = UUID.randomUUID().toString();
                            }

                            WarehouseOrderLine resultOrder = WarehouseOrderLine
                                    .newBuilder()
                                    .setUuid(uuid)
                                    .setCountry(newPurchaseOrderLine.getCountry())
                                    .setDate(newPurchaseOrderLine.getDate())
                                    .setProductUuid(newPurchaseOrderLine.getProductUuid())
                                    .setProductName(newPurchaseOrderLine.getProductName())
                                    .setProductBarCode(newPurchaseOrderLine.getProductBarCode())
                                    .setQuantity(newPurchaseOrderLine.getQuantity())
                                    .build();

                            LOGGER.info(">>> Stream - Warehouse order line generated uuid={}", uuid);

                            return resultOrder;
                        },
                        Materialized.with(
                                stringKeyAvroSerde,
                                warehouseOrderLineValueAvroSerde
                        )
                )
                .toStream()
                .selectKey(
                        (String dummy, WarehouseOrderLine warehouseOrderLine) -> warehouseOrderLine.getUuid()
                );


        warehouseOrderLinesStrem.to(
                warehouseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        return builder;
    }
}
