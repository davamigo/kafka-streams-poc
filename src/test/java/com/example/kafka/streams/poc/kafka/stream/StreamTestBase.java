package com.example.kafka.streams.poc.kafka.stream;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for unit testing Kafka Streams topology
 */
class StreamTestBase {

    /** Topology test driver - tool to test the topology of a Kafka stream */
    TopologyTestDriver testDriver = null;

    /** Avro serializers/deserializers */
    KafkaAvroSerializer keyAvroSerializer;
    KafkaAvroSerializer valueAvroSerializer;
    KafkaAvroDeserializer valueAvroDeserializer;

    /** Constants */
    final String DUMMY_SCHEMA_REGISTRY = "dummy:1234";
    final String DUMMY_SCHEMA_REGISTRY_URL = "http://" + DUMMY_SCHEMA_REGISTRY;

    /**
     * Sets up the environment before testing the Kafka streams process
     *
     * @param schemaRegistryClient the schema registry mock to avoid using the real schema registry
     * @param topology             the topology to test
     */
    void parentSetUp(final SchemaRegistryClient schemaRegistryClient, final Topology topology) {

        // Configure the properties of the Kafka Streams process
        final Properties config = new Properties();
        config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, DUMMY_SCHEMA_REGISTRY);
        config.setProperty(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.setProperty(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
        config.setProperty(StreamsConfig.REPLICATION_FACTOR_CONFIG, "1");
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class.getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class.getName());
        config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        config.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.setProperty(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, DUMMY_SCHEMA_REGISTRY_URL);

        // Create the test driver
        testDriver = new TopologyTestDriver(topology, config);

        // Create de avro serializers/deserializers
        final Map<String, String> serializerConfig = Collections
                .singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, DUMMY_SCHEMA_REGISTRY_URL);

        keyAvroSerializer = new KafkaAvroSerializer(schemaRegistryClient);
        keyAvroSerializer.configure(serializerConfig, true);

        valueAvroSerializer = new KafkaAvroSerializer(schemaRegistryClient);
        valueAvroSerializer.configure(serializerConfig, false);

        valueAvroDeserializer = new KafkaAvroDeserializer(schemaRegistryClient);
        valueAvroDeserializer.configure(serializerConfig, false);
    }

    /**
     * Clears the environment after testing the Kafka streams process
     */
    void parentTearDown() {
        if (null != testDriver) {
            testDriver.close();
        }
    }
}
