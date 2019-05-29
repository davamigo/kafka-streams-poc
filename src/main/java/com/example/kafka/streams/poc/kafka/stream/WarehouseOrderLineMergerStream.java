package com.example.kafka.streams.poc.kafka.stream;

import com.example.kafka.streams.poc.kafka.serde.GenericPrimitiveAvroSerde;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
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
 * Kafka streams to merge the matched warehouse order lines and the recovered warehouse order lines streams into one
 * larger stream.
 */
@Component
public class WarehouseOrderLineMergerStream extends BaseStream {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderLineMergerStream.class);

    /** The name of the matched warehouse order lines Kafka topic (input KStream) */
    private final String matchedWarehouseOrderLinesTopic;

    /** The name of the recovered warehouse order lines Kafka topic (input KStream) */
    private final String recoveredWarehouseOrderLinesTopic;

    /** The name of the new warehouse order lines Kafka topic (output KStream) */
    private final String newWarehouseOrderLinesTopic;

    /** Serde for the string avro key */
    private final Serde<String> stringKeyAvroSerde;

    /** Serde for encoding/decoding a warehouse order line to/from avro */
    private final Serde<WarehouseOrderLine> warehouseOrderLineValueAvroSerde;

    /**
     * Autowired constructor
     *
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param matchedWarehouseOrderLinesTopic   the name of the matched warehouse order lines Kafka topic (input KStream)
     * @param recoveredWarehouseOrderLinesTopic the name of the recovered warehouse order lines Kafka topic (input KStream)
     * @param newWarehouseOrderLinesTopic       the name of the new warehouse order lines Kafka topic (output KStream)
     */
    @Autowired
    public WarehouseOrderLineMergerStream(
            @Value("${spring.kafka.schema-registry-url}") String schemaRegistryUrl,
            @Value("${spring.kafka.topics.warehouse-order-lines-matched}") String matchedWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-recovered}") String recoveredWarehouseOrderLinesTopic,
            @Value("${spring.kafka.topics.warehouse-order-lines-new}") String newWarehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.matchedWarehouseOrderLinesTopic = matchedWarehouseOrderLinesTopic;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;
        this.newWarehouseOrderLinesTopic = newWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>();
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>();

        configureSerdes();
    }

    /**
     * Test constructor
     *
     * @param schemaRegistryClient              the schema registry client (for testing)
     * @param schemaRegistryUrl                 the URL of the schema registry
     * @param matchedWarehouseOrderLinesTopic   the name of the matched warehouse order lines Kafka topic (input KStream)
     * @param recoveredWarehouseOrderLinesTopic the name of the recovered warehouse order lines Kafka topic (input KStream)
     * @param newWarehouseOrderLinesTopic       the name of the new warehouse order lines Kafka topic (output KStream)
     */
    public WarehouseOrderLineMergerStream(
            SchemaRegistryClient schemaRegistryClient,
            String schemaRegistryUrl,
            String matchedWarehouseOrderLinesTopic,
            String recoveredWarehouseOrderLinesTopic,
            String newWarehouseOrderLinesTopic
    ) {
        super(schemaRegistryUrl);

        this.matchedWarehouseOrderLinesTopic = matchedWarehouseOrderLinesTopic;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;
        this.newWarehouseOrderLinesTopic = newWarehouseOrderLinesTopic;

        this.stringKeyAvroSerde = new GenericPrimitiveAvroSerde<>(schemaRegistryClient);
        this.warehouseOrderLineValueAvroSerde = new SpecificAvroSerde<>(schemaRegistryClient);

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
     * - Merges two streams with the same contract into one larger stream. There is no ordering guarantee between
     *   records. Relative order is preserved within each input stream though (ie, records within one input stream
     *   are processed in order).
     *
     * @param builder the streams builder
     * @return the builder configured with the topology
     */
    @Bean("warehouseOrderLineMergerStreamTopology")
    public StreamsBuilder startProcessing(
            @Qualifier("warehouseOrderLineMergerStreamBuilderFactoryBean") StreamsBuilder builder
    ) {
        KStream<String, WarehouseOrderLine> matchedWarehouseOrderLineStream = builder.stream(
                matchedWarehouseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> recoveredWarehouseOrderLineStream = builder.stream(
                recoveredWarehouseOrderLinesTopic,
                Consumed.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        KStream<String, WarehouseOrderLine> newWarehouseOrderLineStream = matchedWarehouseOrderLineStream
                .merge(recoveredWarehouseOrderLineStream);

        newWarehouseOrderLineStream
                .foreach(
                        (String uuid, WarehouseOrderLine line) -> LOGGER.info(">>> Stream - Warehouse order line merged uuid={}!", uuid)
                );

        newWarehouseOrderLineStream.to(
                newWarehouseOrderLinesTopic,
                Produced.with(stringKeyAvroSerde, warehouseOrderLineValueAvroSerde)
        );

        return builder;
    }
}
