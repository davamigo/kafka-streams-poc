package com.example.kafka.streams.poc.kafka.config;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.schemas.product.Product;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for KafkaConfig class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestKafkaConfig {

    /**
     * Mock for Environment
     */
    @Mock
    private Environment environment;

    @Test
    public void testProducerConfigs() {

        // Mocks config
        when(environment.getProperty("spring.kafka.bootstrap-servers")).thenReturn("101");
        when(environment.getProperty("spring.kafka.producer.acks")).thenReturn("102");
        when(environment.getProperty("spring.kafka.producer.retries")).thenReturn("103");
        when(environment.getProperty("spring.kafka.producer.linger-ms")).thenReturn("104");
        when(environment.getProperty("spring.kafka.schema-registry-url")).thenReturn("105");

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        Map<String, Object> props = kafkaConfig.producerConfigs();

        // Assertions
        assertTrue(props.size() >= 7);
        assertEquals("101", props.get("bootstrap.servers"));
        assertEquals("102", props.get("acks"));
        assertEquals("103", props.get("retries"));
        assertEquals("104", props.get("linger.ms"));
        assertEquals("105", props.get("schema.registry.url"));
        assertEquals(KafkaAvroSerializer.class, props.get("key.serializer"));
        assertEquals(KafkaAvroSerializer.class, props.get("value.serializer"));
    }

    @Test
    public void testConsumerConfigs() {

        // Mocks config
        when(environment.getProperty("spring.kafka.bootstrap-servers")).thenReturn("201");
        when(environment.getProperty("spring.kafka.consumer.auto-offset-reset")).thenReturn("202");
        when(environment.getProperty("spring.kafka.consumer.enable-auto-commit")).thenReturn("203");
        when(environment.getProperty("spring.kafka.schema-registry-url")).thenReturn("204");

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        Map<String, Object> props = kafkaConfig.consumerConfigs();

        // Assertions
        assertTrue(props.size() >= 6);
        assertEquals("201", props.get("bootstrap.servers"));
        assertEquals("202", props.get("auto.offset.reset"));
        assertEquals("203", props.get("enable.auto.commit"));
        assertEquals("204", props.get("schema.registry.url"));
        assertEquals(KafkaAvroDeserializer.class, props.get("key.deserializer"));
        assertEquals(KafkaAvroDeserializer.class, props.get("value.deserializer"));
    }

    @Test
    public void testMemberProducerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ProducerFactory<String, Member> factory = kafkaConfig.memberProducerFactory(new HashMap<>());

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testProductProducerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ProducerFactory<String, Product> factory = kafkaConfig.productProducerFactory(new HashMap<>());

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderProducerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ProducerFactory<String, CommercialOrder> factory = kafkaConfig.commercialOrderProducerFactory(new HashMap<>());

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testWarehouseOrderLineProducerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ProducerFactory<String, WarehouseOrderLine> factory = kafkaConfig.warehouseOrderLineProducerFactory(new HashMap<>());

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testMemberConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, Member> factory = kafkaConfig.memberConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testProductConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, Product> factory = kafkaConfig.productConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, CommercialOrder> factory = kafkaConfig.commercialOrderConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderConvertedConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, CommercialOrderConverted> factory = kafkaConfig.commercialOrderConvertedConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderLineSplitConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, CommercialOrderLineSplit> factory = kafkaConfig.commercialOrderLineSplitConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testPurchaseOrderConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, PurchaseOrder> factory = kafkaConfig.purchaseOrderConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testPurchaseOrderLineConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, PurchaseOrderLine> factory = kafkaConfig.purchaseOrderLineConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testWarehouseOrderLineConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, WarehouseOrderLine> factory = kafkaConfig.warehouseOrderLineConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testWarehouseOrderConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, WarehouseOrder> factory = kafkaConfig.warehouseOrderConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testProductLegacyIdConsumerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ConsumerFactory<String, Integer> factory = kafkaConfig.productLegacyIdConsumerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testMemberKafkaTemplate() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaTemplate<String, Member> template = kafkaConfig.memberKafkaProducerTemplate();

        // Assertions
        assertNotNull(template);
    }

    @Test
    public void testProductKafkaTemplate() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaTemplate<String, Product> template = kafkaConfig.productKafkaProducerTemplate();

        // Assertions
        assertNotNull(template);
    }

    @Test
    public void testCommercialOrderKafkaTemplate() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaTemplate<String, CommercialOrder> template = kafkaConfig.commercialOrderKafkaProducerTemplate();

        // Assertions
        assertNotNull(template);
    }

    @Test
    public void testWarehouseOrderLineKafkaTemplate() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaTemplate<String, WarehouseOrderLine> template = kafkaConfig.warehouseOrderLineKafkaProducerTemplate();

        // Assertions
        assertNotNull(template);
    }

    @Test
    public void testMemberKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Member>> factory = kafkaConfig.memberKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testProductKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Product>> factory = kafkaConfig.productKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommercialOrder>> factory = kafkaConfig.commercialOrderKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderConvertedKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommercialOrderConverted>> factory = kafkaConfig.commercialOrderConvertedKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testCommercialOrderLineSplitKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommercialOrderLineSplit>> factory = kafkaConfig.commercialOrderLineSplitKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testPurchaseOrderKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, PurchaseOrder>> factory = kafkaConfig.purchaseOrderKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testPurchaseOrderLineKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, PurchaseOrderLine>> factory = kafkaConfig.purchaseOrderLineKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testWarehouseOrderLineKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, WarehouseOrderLine>> factory = kafkaConfig.warehouseOrderLineKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testWarehouseOrderKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, WarehouseOrder>> factory = kafkaConfig.warehouseOrderKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testProductLegacyIdKafkaListenerContainerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Integer>> factory = kafkaConfig.productLegacyIdKafkaListenerContainerFactory();

        // Assertions
        assertNotNull(factory);
    }
}
