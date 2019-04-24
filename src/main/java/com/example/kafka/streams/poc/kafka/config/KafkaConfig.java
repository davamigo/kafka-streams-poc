package com.example.kafka.streams.poc.kafka.config;

import com.example.kafka.streams.poc.schemas.product.Product;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Apache Kafka and Kafka Streams
 */
@EnableKafka
@Configuration
public class KafkaConfig {

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
    public KafkaConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Global configuration values for all Kafka producers
     *
     * @return the default configurations for all the Kafka producers
     */
    Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ProducerConfig.ACKS_CONFIG, environment.getProperty("spring.kafka.producer.acks"));
        props.put(ProducerConfig.RETRIES_CONFIG, environment.getProperty("spring.kafka.producer.retries"));
        props.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("spring.kafka.producer.linger-ms"));
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, environment.getProperty("spring.kafka.schema-registry-url"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        return props;
    }

    /**
     * Creates a factory for a Kafka producer
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, Product> productProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Kafka template for producing messages
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, Product> productKafkaProducerTemplate() {
        return new KafkaTemplate<>(productProducerFactory(producerConfigs()));
    }
}
