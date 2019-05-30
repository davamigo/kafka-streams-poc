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
 * Kafka streams for matching each purchase order line with the product legacy id while generating the warehouse order lines
 */
@Component
public class WarehouseOrderLineProductMatcherStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderLineProductMatcherStream.class);

    /** The name of the aggregated purchase order lines Kafka topic (input KStream) */
    private final String aggregatedPurchaseOrderLinesTopic;

    /** The name of the product legacy id cache Kafka topic (input KTable) */
    private final String productLegacyIdsCacheTopic;

    /** The name of the matched warehouse order lines Kafka topic (output KStream) */
    private final String matchedWarehouseOrderLinesTopic;

    /** The name of the unmatched warehouse order lines Kafka topic (output KStream) */
    private final String unmatchedWarehouseOrderLinesTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for encoding/decoding a purchase order line to/from avro */
    private final Serde<PurchaseOrderLine> purchaseOrderLineValueAvroSerde;

    /** Serde for encoding/decoding an integer to/from avro */
    private final Serde<Integer> integerValueAvroSerde;

    /** Serde for encoding/decoding a warehouse order line to/from avro */
    private final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (input KStream)
     * @param productLegacyIdsCacheTopic        the name of the product legacy id cache Kafka topic (input KTable)
     * @param matchedWarehouseOrderLinesTopic   the name of the matched warehouse order lines Kafka topic (output KStream)
     * @param unmatchedWarehouseOrderLinesTopic the name of the unmatched warehouse order lines Kafka topic (output KStream)
     */
    @Autowired
    public WarehouseOrderLineProductMatcherStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.purchase-order-lines-aggregated}") String aggregatedPurchaseOrderLinesTopic,
            @Value("${spring.kafka.topics.product-legacy-ids}") String productLegacyIdsCacheTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-matched}") String matchedWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-unmatched}") String unmatchedWarehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;
        this.productLegacyIdsCacheTopic = productLegacyIdsCacheTopic;
        this.matchedWarehouseOrderLinesTopic = matchedWarehouseOrderLinesTopic;
        this.unmatchedWarehouseOrderLinesTopic = unmatchedWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>();
        this.integerValueAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient              the schema registry client (for testing)
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param aggregatedPurchaseOrderLinesTopic the name of the aggregated purchase order lines Kafka topic (input KStream)
     * @param productLegacyIdsCacheTopic        the name of the product legacy id cache Kafka topic (input KTable)
     * @param matchedWarehouseOrderLinesTopic   the name of the matched warehouse order lines Kafka topic (output KStream)
     * @param unmatchedWarehouseOrderLinesTopic the name of the unmatched warehouse order lines Kafka topic (output KStream)
     */
    public WarehouseOrderLineProductMatcherStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String aggregatedPurchaseOrderLinesTopic,
            String productLegacyIdsCacheTopic,
            String matchedWarehouseOrderLinesTopic,
            String unmatchedWarehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.aggregatedPurchaseOrderLinesTopic = aggregatedPurchaseOrderLinesTopic;
        this.productLegacyIdsCacheTopic = productLegacyIdsCacheTopic;
        this.matchedWarehouseOrderLinesTopic = matchedWarehouseOrderLinesTopic;
        this.unmatchedWarehouseOrderLinesTopic = unmatchedWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.purchaseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);
        this.integerValueAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        stringKeyAvroSerde.configure(serdeConfig, true);
        purchaseOrderLineValueAvroSerde.configure(serdeConfig, false);
        integerValueAvroSerde.configure(serdeConfig, false);
        warehouseOrderLineValueAvroSerde.configure(serdeConfig, false);
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
     * - The branch operation will divide the kstraem depending if the product legacy-id was found.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("warehouseOrderLineProductMatcherStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("warehouseOrderLineProductMatcherStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, PurchaseOrderLine> purchaseOrderLinesStream = builder.stream(
                aggregatedPurchaseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, purchaseOrderLineValueAvroSerde)
        );

        KTable<String, Integer> productLecgaryIdsTable = builder.table(
                productLegacyIdsCacheTopic,
                Consumed.with(stringKeyAvroSerde, integerValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesBase = purchaseOrderLinesStream
                .selectKey(
                        (String lineUuid, PurchaseOrderLine line) -> line.getProductUuid()
                )
                .leftJoin(
                        productLecgaryIdsTable,
                        (PurchaseOrderLine line, Integer productLegacyId) -> {
                            if (null != productLegacyId) {
                                LOGGER.info(">>> Stream - Warehouse order line matcher - Product legacy id. matched for product uuid={} - legacy-id={}", line.getProductUuid(), productLegacyId);
                            }
                            else {
                                LOGGER.info(">>> Stream - Warehouse order line matcher - Product legacy id. not matched for product uuid={}!", line.getProductUuid());
                            }

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

        return builder;
    }
}
