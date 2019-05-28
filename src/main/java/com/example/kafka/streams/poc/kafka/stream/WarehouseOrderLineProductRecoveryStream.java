package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.service.api.LegacyProductIdsApiInterface;
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

/**
 * Kafka streams for recovery product legacy ids for each unmatched warehouse order lines calling an external API
 */
@Component
public class WarehouseOrderLineProductRecoveryStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderLineProductRecoveryStream.class);

    /** The service to get the legacy Id. of a product from the Uuid */
    private final LegacyProductIdsApiInterface legacyProductIdsApi;

    /** The name of the unmatched warehouse order lines Kafka topic (input KStream) */
    private final String unmatchedWarehouseOrderLinesTopic;

    /** The name of the recovered warehouse order lines Kafka topic (output KStream) */
    private final String recoveredWarehouseOrderLinesTopic;

    /** The name of the failed warehouse order lines Kafka topic (output KStream) */
    private final String failedWarehouseOrderLinesTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for encoding/decoding a warehouse order line to/from avro */
    private final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param unmatchedWarehouseOrderLinesTopic the name of the unmatched warehouse order lines Kafka topic (input KStream)
     * @param recoveredWarehouseOrderLinesTopic the name of the recovered warehouse order lines Kafka topic (output KStream)
     * @param failedWarehouseOrderLinesTopic    the name of the failed warehouse order lines Kafka topic (output KStream)
     * @param legacyProductIdsApi               the service to get the legacy Id. of a product from the Uuid
     */
    @Autowired
    public WarehouseOrderLineProductRecoveryStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.warehouse-order-lines-unmatched}") String unmatchedWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-recovered}") String recoveredWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-failed}") String failedWarehouseOrderLinesTopic,
            LegacyProductIdsApiInterface legacyProductIdsApi
    ) {
        super(schemaRegistryUrl);

        this.unmatchedWarehouseOrderLinesTopic = unmatchedWarehouseOrderLinesTopic;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;
        this.failedWarehouseOrderLinesTopic = failedWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        this.legacyProductIdsApi = legacyProductIdsApi;

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient              the schema registry client (for testing)
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param unmatchedWarehouseOrderLinesTopic the name of the unmatched warehouse order lines Kafka topic (input KStream)
     * @param recoveredWarehouseOrderLinesTopic the name of the recovered warehouse order lines Kafka topic (output KStream)
     * @param failedWarehouseOrderLinesTopic    the name of the failed warehouse order lines Kafka topic (output KStream)
     * @param legacyProductIdsApi               the service to get the legacy Id. of a product from the Uuid
     */
    public WarehouseOrderLineProductRecoveryStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String unmatchedWarehouseOrderLinesTopic,
            String recoveredWarehouseOrderLinesTopic,
            String failedWarehouseOrderLinesTopic,
            LegacyProductIdsApiInterface legacyProductIdsApi
    ) {
        super(schemaRegistryUrl);

        this.unmatchedWarehouseOrderLinesTopic = unmatchedWarehouseOrderLinesTopic;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;
        this.failedWarehouseOrderLinesTopic = failedWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        this.legacyProductIdsApi = legacyProductIdsApi;

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        stringKeyAvroSerde.configure(serdeConfig, true);
        warehouseOrderLineValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - The mapValues operation will call an external API per each record to find the legacy-id.
     *
     *   The result is a kstream with the same key and the same valur + the legacy id if found.
     *
     * - The branch operation will divide the kstraem depending if the product legacy-id was found.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("warehouseOrderLineProductRecoveryStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("warehouseOrderLineProductRecoveryStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, WarehouseOrderLine> warehouseOrderLineStream = builder.stream(
                unmatchedWarehouseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesMapped = warehouseOrderLineStream
                .mapValues(
                        (String lineUuid, WarehouseOrderLine line) -> {
                            WarehouseOrderLine.Builder lineBuilder  = WarehouseOrderLine.newBuilder(line);
                            Integer productLegacyId = legacyProductIdsApi.getLegacyId(line.getProductUuid()).orElse(null);
                            if (null != productLegacyId) {
                                LOGGER.info(">>> Stream - Warehouse order line recovery - Product legacy id. found for product uuid={} - legacy-id={}", line.getProductUuid(), productLegacyId);
                                lineBuilder.setProductLegacyId(productLegacyId);
                            } else {
                                LOGGER.info(">>> Stream - Warehouse order line recovery - Product legacy id. not found for product uuid={}!", line.getProductUuid());
                            }
                            return lineBuilder.build();
                        }
                );

        @SuppressWarnings("unchecked")
        KStream<String, WarehouseOrderLine>[] warehouseOrderLinesBranches = warehouseOrderLinesMapped
                .branch(
                        (String productUuid, WarehouseOrderLine line) -> line.getProductLegacyId() != null,
                        (String productUuid, WarehouseOrderLine line) -> true
                );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesRecovered = warehouseOrderLinesBranches[0];
        warehouseOrderLinesRecovered.to(
                recoveredWarehouseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> warehouseOrderLinesFailed = warehouseOrderLinesBranches[1];
        warehouseOrderLinesFailed.to(
                failedWarehouseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        return builder;
    }
}
