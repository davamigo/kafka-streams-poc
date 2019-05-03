package com.example.kafka.streams.poc.kafka.stream;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

import java.util.Collections;
import java.util.Map;

/**
 * Kafka streams base class
 */
abstract public class BaseStream {

    /** The configuration for serdes */
    protected final Map<String, String> serdeConfig;

    /**
     * Default constructor
     *
     * @param schemaRegistryUrl the URL of the schema registry
     */
    public BaseStream(String schemaRegistryUrl) {
        this.serdeConfig = Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
    }
}
