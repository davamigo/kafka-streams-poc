package com.example.kafka.streams.poc.kafka.config;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for all Kafka Streams classes
 */
@Configuration
public class KafkaStreamsConfig {

    /**
     * The environment object where to get the config options
     */
    private Environment environment;

    /**
     * Autowired Constructor
     *
     * @param environment The environment object where to get the config options
     */
    @Autowired
    public KafkaStreamsConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Global configuration values for all Kafka streams classes
     *
     * @return the default configurations for all the Kafka producers
     */
    Map<String, Object> streamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, environment.getProperty("spring.kafka.streams.threads"));
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "default");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, environment.getProperty("spring.kafka.schema-registry-url"));
        return props;
    }

    /**
     * Creates a bean for a Kafka Streams process to convert the commercial orders
     *
     * @return A factory to build the stream
     */
    @Bean("commercialOrderConverterStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean commercialOrderConverterStreamBuilderFactoryBean() {
        Map<String, Object> props = streamsConfigs();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "commercialOrderConverterStream");
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }

    /**
     * Creates a bean for a Kafka Streams process to convert the commercial orders
     *
     * @return A factory to build the stream
     */
    @Bean("commercialOrderLineSplitStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean commercialOrderLineSplitStreamBuilderFactoryBean() {
        Map<String, Object> props = streamsConfigs();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "commercialOrderLineSplitStream");
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }
}