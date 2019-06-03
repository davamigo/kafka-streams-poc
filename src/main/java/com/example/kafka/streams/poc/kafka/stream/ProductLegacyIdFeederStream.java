package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Kafka streams for feeding the product-legacy-id topic from the recovered warehouse order lines stream.
 */
@Component
public class ProductLegacyIdFeederStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLegacyIdFeederStream.class);

    /** The name of the recovered warehouse order lines Kafka topic (input KStream) */
    private final String recoveredWarehouseOrderLinesTopic;

    /** The name of the product legacy id cache Kafka topic (output KTable) */
    private final String productLegacyIdsCacheTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for encoding/decoding an integer to/from avro */
    private final Serde<Integer> integerValueAvroSerde;

    /** Serde for encoding/decoding a warehouse order line to/from avro */
    private final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param recoveredWarehouseOrderLinesTopic the name of the recovered warehouse order lines Kafka topic (input KStream)
     * @param productLegacyIdsCacheTopic        the name of the product legacy id cache Kafka topic (output KTable)
     */
    @Autowired
    public ProductLegacyIdFeederStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.warehouse-order-lines-recovered}") String recoveredWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.product-legacy-ids}") String productLegacyIdsCacheTopic
    ) {
        super(schemaRegistryUrl);

        this.productLegacyIdsCacheTopic = productLegacyIdsCacheTopic;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.integerValueAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient              the schema registry client (for testing)
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param recoveredWarehouseOrderLinesTopic the name of the recovered warehouse order lines Kafka topic (input KStream)
     * @param productLegacyIdsCacheTopic        the name of the product legacy id cache Kafka topic (output KTable)
     */
    public ProductLegacyIdFeederStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String recoveredWarehouseOrderLinesTopic,
            String productLegacyIdsCacheTopic
    ) {
        super(schemaRegistryUrl);

        this.productLegacyIdsCacheTopic = productLegacyIdsCacheTopic;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.integerValueAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

        configureSerdes();
    }

    /**
     * Configures all the serdes for this Kafka Streams
     */
    private void configureSerdes() {
        stringKeyAvroSerde.configure(serdeConfig, true);
        integerValueAvroSerde.configure(serdeConfig, false);
        warehouseOrderLineValueAvroSerde.configure(serdeConfig, false);
    }

    /**
     * Builds the topology of the Kafka Streams.
     *
     * - The map operation creates a new stream from the product uuid and the product legacy id.
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("productLegacyIdFeederStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("productLegacyIdFeederStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, WarehouseOrderLine> warehouseOrderLinesStream = builder.stream(
                recoveredWarehouseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KStream<String, Integer> productLegacyIdsStream = warehouseOrderLinesStream.map(
                (String dummy, WarehouseOrderLine line) -> KeyValue.pair(line.getProductUuid(), line.getProductLegacyId())
        );

        productLegacyIdsStream.foreach(
                (String productUuid, Integer productLegacyId) -> LOGGER.info(">>> Stream - Product legacy id stored in cache uuid={} legacy-id={}", productUuid, productLegacyId)
        );

        productLegacyIdsStream.to(
                productLegacyIdsCacheTopic,
                Produced.with(stringKeyAvroSerde, integerValueAvroSerde)
        );

        return builder;
    }
}
