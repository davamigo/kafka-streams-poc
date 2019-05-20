package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
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
 * Kafka streams for matching each purchase order line with the product legacy id while generating the warehouse order lines
 */
@Component
public class WarehouseOrderProductMatcherStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderProductMatcherStream.class);

    /** The name of the aggregated purchase order lines Kafka topic (input KStream) */
    private final String aggregatedPurchaseOrderLinesTopic;

    /** The name of the product legacy id cache Kafka topic (input KTable) */
    private final String productsLegacyIdCacheTopic;

    /** The name of the matched warehouse order lines Kafka topic (output KStream) */
    private final String matchedWarehouseOrderLinesTopic;

    /** The name of the unmatched warehouse order lines Kafka topic (output KStream) */
    private final String unmatchedWarehouseOrderLinesTopic;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (input KStream)
     * @param matchedWarehouseOrderLinesTopic       the name of the generated purchase order Kafka topic (output KStream)
     */
    @Autowired
    public WarehouseOrderProductMatcherStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.purchase-order-lines-aggregated}") String aggregatedPurchaseOrderLinesTopic,
            @Value("${spring.kafka.topics.products-legacy-id}") String productsLegacyIdCacheTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-matched}") String matchedWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-unmatched}") String unmatchedWarehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);
        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;
        this.productsLegacyIdCacheTopic = productsLegacyIdCacheTopic;
        this.matchedWarehouseOrderLinesTopic = matchedWarehouseOrderLinesTopic;
        this.unmatchedWarehouseOrderLinesTopic = unmatchedWarehouseOrderLinesTopic;
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - The left join operation purchase order lines kstream with product legacy-id ktable by product-uuid
     *
     *   The result is a kstream with a Warehouse Order Line contract.
     *
     *   If matched the product legacy-id is saved to the contract.
     *   If not, the legacy-id will be null.
     *
     *  - branch operation will divide the kstrem depending if the product legacy-id was found.
     *
     * @param builder the streams builder
     * @return the result KStream
     */
    @Bean("warehouseOrderProductMatcherStreamTopology")
    public KStream<String, WarehouseOrderLine> startProcessing(
            @Qualifier("warehouseOrderProductMatcherStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        final Serde<String> stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        stringKeyAvroSerde.configure(serdeConfig, true);

        final Serde<PurchaseOrderLine> purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        purchaseOrderLineValueAvroSerde.configure(serdeConfig, false);

        final Serde<Integer> integerValueAvroSerde = new GenericPrimitiveAvroSerde<>();
        integerValueAvroSerde.configure(serdeConfig, false);

        final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        warehouseOrderLineValueAvroSerde.configure(serdeConfig, false);

        KStream<String, PurchaseOrderLine> purchaseOrderLinesStream = builder.stream(
                aggregatedPurchaseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
        );

        KTable<String, Integer> productLecgaryIdsTable = builder.table(
                productsLegacyIdCacheTopic,
                Consumed.with(stringKeyAvroSerde, integerValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesBase = purchaseOrderLinesStream
                .selectKey(
                        (String lineUuid, PurchaseOrderLine line) -> line.getProductUuid()
                )
                .leftJoin(
                        productLecgaryIdsTable,
                        (PurchaseOrderLine line, Integer productLegacyId) -> {
                            LOGGER.info(">>> Stream - Commercial order line uuid={} left joined with product uuid={} - Matched legacy-id={}",
                                    line.getUuid(),
                                    line.getProductUuid(),
                                    productLegacyId
                            );

                            return WarehouseOrderLine
                                    .newBuilder()
                                    .setUuid(UUID.randomUUID().toString())
                                    .setCountry(line.getCountry())
                                    .setDate(line.getDate())
                                    .setProductUuid(line.getProductUuid())
                                    .setProductLegacyId(productLegacyId)
                                    .setProductName(line.getProductName())
                                    .setProductBarCode(line.getProductBarCode())
                                    .setQuantity(line.getQuantity())
                                    .build();
                        },
                        Joined.with(
                                stringKeyAvroSerde,
                                purchaseOrderLineValueAvroSerde,
                                integerValueAvroSerde
                        )
                ).selectKey(
                        (String dummy, WarehouseOrderLine line) -> line.getUuid()
                );

        @SuppressWarnings("unchecked")
        KStream<String, WarehouseOrderLine>[] warehouseOrderLinesBranches = warehouseOrderLinesBase
                .branch(
                        (String productUuid, WarehouseOrderLine line) -> null != line.getProductLegacyId(),
                        (String productUuid, WarehouseOrderLine line) -> true
                );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesMatched = warehouseOrderLinesBranches[0];
        warehouseOrderLinesMatched.to(
                matchedWarehouseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesUnmatched = warehouseOrderLinesBranches[1];
        warehouseOrderLinesUnmatched.to(
                unmatchedWarehouseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        return warehouseOrderLinesMatched;
    }
}
