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
    private Map<String, Object> streamsConfigs() {
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
     * Create custom streams builder factory
     *
     * @param applicationId an identifier for the stream processing application.
     * @return A factory to build the stream process
     */
    private StreamsBuilderFactoryBean newCustomStreamsBuilderFactoryBean(String applicationId) {

        Map<String, Object> props = streamsConfigs();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);

        StreamsBuilderFactoryBean bean = new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
        bean.setAutoStartup(Boolean.parseBoolean(environment.getProperty("spring.kafka.streams.auto-startup", "true")));

        return bean;
    }

    /**
     * Creates a bean for a Kafka Streams process to convert the commercial orders
     *
     * @return A factory to build the stream process
     */
    @Bean("commercialOrderConverterStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean commercialOrderConverterStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("commercialOrderConverterStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to split the commercial order lines
     *
     * @return A factory to build the stream process
     */
    @Bean("commercialOrderLineSplitStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean commercialOrderLineSplitStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("commercialOrderLineSplitStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to generate the purchase order lines
     *
     * @return A factory to build the stream process
     */
    @Bean("purchaseOrderLineAggregatedStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean purchaseOrderLineAggregatedStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("purchaseOrderLineAggregatedStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to generate the purchase order from the aggregated purchase order
     * lines.
     *
     * @return A factory to build the stream process
     */
    @Bean("purchaseOrderGeneratedStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean purchaseOrderGeneratedStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("purchaseOrderGeneratedStream");
    }

    /**
     * Creates a bean for a Kafka Streams process for generating the warehouse order lines from the purchase order lines
     *
     * @return A factory to build the stream process
     */
    @Bean("warehouseOrderLineGeneratorStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean warehouseOrderLineGeneratorStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("warehouseOrderLineGeneratorStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to match the warehouse order lines with the product legacy id.
     *
     * @return A factory to build the stream process
     */
    @Bean("warehouseOrderLineProductMatcherStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean warehouseOrderLineProductMatcherStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("warehouseOrderLineProductMatcherStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to recovery the unmatched warehouse order lines calling an external
     * API to get the product legacy id.
     *
     * @return A factory to build the stream process
     */
    @Bean("warehouseOrderLineProductRecoveryStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean warehouseOrderLineProductRecoveryStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("warehouseOrderLineProductRecoveryStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to merge the matched warehouse order lines and the recovered warehouse
     * order lines streams into one larger stream.
     *
     * @return A factory to build the stream process
     */
    @Bean("warehouseOrderLineMergerStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean warehouseOrderLineMergerStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("warehouseOrderLineMergerStream");
    }

    /**
     * Creates a bean for a Kafka Streams process to generate the warehouse order from the warehouse order lines.
     *
     * @return A factory to build the stream process
     */
    @Bean("warehouseOrdersGeneratorStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean warehouseOrdersGeneratorStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("warehouseOrdersGeneratorStream");
    }

    /**
     * Creates a bean for a Kafka Streams process feeding the product-legacy-id topic from the recovered warehouse order
     * lines stream.
     *
     * @return A factory to build the stream process
     */
    @Bean("productLegacyIdFeederStreamBuilderFactoryBean")
    public StreamsBuilderFactoryBean productLegacyIdFeederStreamBuilderFactoryBean() {
        return newCustomStreamsBuilderFactoryBean("productLegacyIdFeederStream");
    }
}
