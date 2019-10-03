package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Kafka streams for generating the purchase order from the aggregated purchase order lines
 */
@Component
public class PurchaseOrderGeneratorStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderGeneratorStream.class);

    /** The name of the aggregated purchase order lines Kafka topic (input KStream) */
    private final String aggregatedPurchaseOrderLinesTopic;

    /** The name of the generated purchase order Kafka topic (output KStream) */
    private final String generatedPurchaseOrdersTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for the input purchase order line avro value */
    private final Serde<PurchaseOrderLine> purchaseOrderLineValueAvroSerde;

    /** Serde for the output purchase order avro value */
    private final Serde<PurchaseOrder> purchaseOrderValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (input KStream)
     * @param generatedPurchaseOrdersTopic      the name of the generated purchase order Kafka topic (output KStream)
     */
    @Autowired
    public PurchaseOrderGeneratorStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.purchase-order-lines-aggregated}") String aggregatedPurchaseOrderLinesTopic,
            @Value("${spring.kafka.topics.purchase-orders-generated}") String generatedPurchaseOrdersTopic
    ) {
        super(schemaRegistryUrl);

        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;
        this.generatedPurchaseOrdersTopic = generatedPurchaseOrdersTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        this.purchaseOrderValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient              the schema registry client (for testing)
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (input KStream)
     * @param generatedPurchaseOrdersTopic       the name of the generated purchase order Kafka topic (output KStream)
     */
    public PurchaseOrderGeneratorStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String aggregatedPurchaseOrderLinesTopic,
            String generatedPurchaseOrdersTopic
    ) {
        super(schemaRegistryUrl);

        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;
        this.generatedPurchaseOrdersTopic = generatedPurchaseOrdersTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.purchaseOrderValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        this.stringKeyAvroSerde.configure(serdeConfig, true);
        this.purchaseOrderLineValueAvroSerde.configure(serdeConfig, false);
        this.purchaseOrderValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - The groupBy operation will group all the purchase order lines by a new key
     *
     *   The result is a KGrouped stream which will be used to aggregate the purchase order lines in a purchase order.
     *
     *   The new key will be composed by the country code and the date (one per day)
     *
     *   Key format: CC-YYYY-MM-DD-PPPPPP, where:
     *   - CC - The country code.
     *   - YYYY-MM-DD - The date.
     *
     * - The aggregate operation will add all the purchase order lines to an array in the purchase order.
     *
     *   So there will be a purchase order per country and day with all the lines.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("purchaseOrderGeneratedStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("purchaseOrderGeneratedStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, PurchaseOrderLine> purchaseOrderLinesStream = builder.stream(
                aggregatedPurchaseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
        );

        KGroupedStream<String, PurchaseOrderLine> purchaseOrderLinesGroupedStream = purchaseOrderLinesStream
                .groupBy(
                        (String poLineAggregationKey, PurchaseOrderLine poLine) -> {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date datetime = new Date(poLine.getDate());
                            return poLine.getCountry() + "-" + df.format(datetime);
                        },
                        Serialized.with(
                                stringKeyAvroSerde,
                                purchaseOrderLineValueAvroSerde
                        )
                );

        KStream<String, PurchaseOrder> purchaseOrderStream = purchaseOrderLinesGroupedStream
                .aggregate(
                        PurchaseOrder::new,
                        (String aggregationKey, PurchaseOrderLine newPOLine, PurchaseOrder aggregatedPO) -> {

                            String uuid = aggregatedPO.getUuid();
                            if (null == uuid) {
                                uuid = UUID.randomUUID().toString();
                            }

                            float newAmount = aggregatedPO.getTotalAmount() + (newPOLine.getProductPrice() * newPOLine.getQuantity());
                            int newQuantity = aggregatedPO.getTotalQuantity() + newPOLine.getQuantity();

                            List<PurchaseOrderLineCondensed> newLines = new ArrayList<>();
                            if (null != aggregatedPO.getLines()) {
                                for (PurchaseOrderLineCondensed oldLine : aggregatedPO.getLines()) {
                                    if (!oldLine.getProductUuid().equals(newPOLine.getProductUuid())) {
                                        newLines.add(PurchaseOrderLineCondensed.newBuilder(oldLine).build());
                                    }
                                    else {
                                        newAmount -= (oldLine.getPrice() * oldLine.getQuantity());
                                        newQuantity -= oldLine.getQuantity();
                                    }
                                }
                            }

                            newLines.add(
                                    PurchaseOrderLineCondensed
                                            .newBuilder()
                                            .setUuid(newPOLine.getUuid())
                                            .setAggregationKey(newPOLine.getAggregationKey())
                                            .setProductUuid(newPOLine.getProductUuid())
                                            .setPrice(newPOLine.getProductPrice())
                                            .setQuantity(newPOLine.getQuantity())
                                            .build()
                            );

                            PurchaseOrder result = PurchaseOrder
                                    .newBuilder(aggregatedPO)
                                    .setUuid(uuid)
                                    .setAggregationKey(aggregationKey)
                                    .setCountry(newPOLine.getCountry())
                                    .setDate(newPOLine.getDate())
                                    .setLines(newLines)
                                    .setTotalAmount(newAmount)
                                    .setTotalQuantity(newQuantity)
                                    .build();

                            LOGGER.info(">>> Stream - Purchase order uuid={} aggregation-key={} - Aggregating purchase order line uuid={}...", uuid, aggregationKey, newPOLine.getUuid());

                            return result;
                        },
                        Materialized.with(
                                stringKeyAvroSerde,
                                purchaseOrderValueAvroSerde
                        )
                )
                .toStream();

        purchaseOrderStream.to(
                generatedPurchaseOrdersTopic,
                Produced.with(stringKeyAvroSerde, purchaseOrderValueAvroSerde)
        );

        return builder;
    }
}
