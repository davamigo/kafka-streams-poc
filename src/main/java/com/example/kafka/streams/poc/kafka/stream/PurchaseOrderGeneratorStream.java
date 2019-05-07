package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private final String generatedPurchaseOrdesTopic;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (input KStream)
     * @param generatedPurchaseOrdesTopic       the name of the generated purchase order Kafka topic (output KStream)
     */
    @Autowired
    public PurchaseOrderGeneratorStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.purchase-order-lines-aggregated}") String aggregatedPurchaseOrderLinesTopic,
            @Value("${spring.kafka.topics.purchase-orders-generated}") String generatedPurchaseOrdesTopic
    ) {
        super(schemaRegistryUrl);
        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;
        this.generatedPurchaseOrdesTopic = generatedPurchaseOrdesTopic;
    }

    /**
     * Builds the topology of the Kafka Streams
     *
     * @param builder the streams builder
     * @return the result KStream
     */
    @Bean("purchaseOrderGeneratedStreamTopology")
    public KStream<String, PurchaseOrder> startProcessing(
            @Qualifier("purchaseOrderGeneratedStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        final Serde<String> stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        stringKeyAvroSerde.configure(serdeConfig, true);

        final Serde<PurchaseOrderLine> purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        purchaseOrderLineValueAvroSerde.configure(serdeConfig, false);

        final Serde<PurchaseOrder> purchaseOrderValueAvroSerde = new SpecificAvroSerde<>();
        purchaseOrderValueAvroSerde.configure(serdeConfig, false);

        KStream<String, PurchaseOrderLine> purchaseOrderLinesStream = builder.stream(
                aggregatedPurchaseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
        );

        KGroupedStream<String, PurchaseOrderLine> purchaseOrderLinesGroupedStream = purchaseOrderLinesStream
                .groupBy(
                        (String key, PurchaseOrderLine line) -> {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date datetime = new Date(line.getDate());
                            return line.getCountry() + "-" + df.format(datetime);
                        },
                        Serialized.with(
                                stringKeyAvroSerde,
                                purchaseOrderLineValueAvroSerde
                        )
                );

        KStream<String, PurchaseOrder> purchaseOrderStream = purchaseOrderLinesGroupedStream
                .aggregate(
                        PurchaseOrder::new,
                        (String key, PurchaseOrderLine newLine, PurchaseOrder purchaseOrder) -> {

                            float newAmount = purchaseOrder.getTotalAmount();
                            int newQuantity = purchaseOrder.getTotalQuantity();

                            List<PurchaseOrderLineCondensed> newLines = new ArrayList<>();
                            if (null != purchaseOrder.getLines()) {
                                for (PurchaseOrderLineCondensed oldLine : purchaseOrder.getLines()) {
                                    if (!oldLine.getProductUuid().equals(newLine.getProductUuid())) {
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
                                            .setPurchaseOrderLineKey(newLine.getKey())
                                            .setProductUuid(newLine.getProductUuid())
                                            .setPrice(newLine.getProductPrice())
                                            .setQuantity(newLine.getQuantity())
                                            .build()
                            );

                            newAmount += newLine.getProductPrice() * newLine.getQuantity();
                            newQuantity += newLine.getQuantity();

                            return PurchaseOrder
                                    .newBuilder(purchaseOrder)
                                    .setKey(key)
                                    .setCountry(newLine.getCountry())
                                    .setDate(newLine.getDate())
                                    .setLines(newLines)
                                    .setTotalAmount(newAmount)
                                    .setTotalQuantity(newQuantity)
                                    .build();
                        },
                        Materialized.with(
                                stringKeyAvroSerde,
                                purchaseOrderValueAvroSerde
                        )
                )
                .toStream();

        purchaseOrderStream.to(
                generatedPurchaseOrdesTopic,
                Produced.with(stringKeyAvroSerde, purchaseOrderValueAvroSerde)
        );

        return purchaseOrderStream;
    }
}
