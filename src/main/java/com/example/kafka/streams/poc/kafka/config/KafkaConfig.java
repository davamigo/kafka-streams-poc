package com.example.kafka.streams.poc.kafka.config;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted;
import com.example.kafka.streams.poc.schemas.product.Product;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

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
     * Global configuration values for all Kafka consumers
     *
     * @return the default configurations for all the Kafka consumers
     */
    Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, environment.getProperty("spring.kafka.consumer.auto-offset-reset"));
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, environment.getProperty("spring.kafka.consumer.enable-auto-commit"));
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, environment.getProperty("spring.kafka.schema-registry-url"));
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);

        return props;
    }

    /**
     * Creates a factory for producing Member messages to Kafka
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, Member> memberProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Creates a factory for producing Product messages to Kafka
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, Product> productProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Creates a factory for producing CommercialOrder messages to Kafka
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, CommercialOrder> commercialOrderProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Creates a factory for producing WarehouseOrderLine messages to Kafka
     *
     * @param props the configuration for the Kafka producer
     * @return the default kafka producer factory
     */
    ProducerFactory<String, WarehouseOrderLine> warehouseOrderLineProducerFactory(Map<String, Object> props) {
        return new DefaultKafkaProducerFactory<>(props);
    }

    /**
     * Creates a factory for consuming Member messages from Kafka
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, Member> memberConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * Creates a factory for consuming Product messages from Kafka
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, Product> productConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * Creates a factory for consuming CommercialOrder messages from Kafka
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, CommercialOrder> commercialOrderConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * Creates a factory for consuming CommercialOrderConverted messages from Kafka
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, CommercialOrderConverted> commercialOrderConvertedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * Creates a factory for consuming PurchaseOrder messages from Kafka
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, PurchaseOrder> purchaseOrderConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * Creates a factory for consuming WarehouseOrderLine messages from Kafka
     *
     * @return the default kafka consumer factory.
     */
    ConsumerFactory<String, WarehouseOrderLine> warehouseOrderLineConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * Kafka template bean for producing Member messages to Kafka
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, Member> memberKafkaProducerTemplate() {
        return new KafkaTemplate<>(memberProducerFactory(producerConfigs()));
    }

    /**
     * Kafka template bean for producing Product messages to Kafka
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, Product> productKafkaProducerTemplate() {
        return new KafkaTemplate<>(productProducerFactory(producerConfigs()));
    }

    /**
     * Kafka template bean for producing CommercialOrder messages to Kafka
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, CommercialOrder> commercialOrderKafkaProducerTemplate() {
        return new KafkaTemplate<>(commercialOrderProducerFactory(producerConfigs()));
    }

    /**
     * Kafka template bean for producing WarehouseOrderLine messages to Kafka
     *
     * @return a new Kafka template for producing messages
     */
    @Bean
    public KafkaTemplate<String, WarehouseOrderLine> warehouseOrderLineKafkaProducerTemplate() {
        return new KafkaTemplate<>(warehouseOrderLineProducerFactory(producerConfigs()));
    }

    /**
     * Kafka listener container factory bean for consuming Member messages from Kafka
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Member>> memberKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Member> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(memberConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Kafka listener container factory bean for consuming Product messages from Kafka
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Product>> productKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Product> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(productConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Kafka listener container factory bean for consuming CommercialOrder messages from Kafka
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommercialOrder>> commercialOrderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommercialOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commercialOrderConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Kafka listener container factory bean for consuming CommercialOrderConverted messages from Kafka
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommercialOrderConverted>> commercialOrderConvertedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CommercialOrderConverted> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commercialOrderConvertedConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Kafka listener container factory bean for consuming PurchaseOrder messages from Kafka
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, PurchaseOrder>> purchaseOrderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PurchaseOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(purchaseOrderConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Kafka listener container factory bean for consuming WarehouseOrderLine messages from Kafka
     *
     * @return the kafka listener container factory for consumer.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, WarehouseOrderLine>> warehouseOrderLineKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, WarehouseOrderLine> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(warehouseOrderLineConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
}
