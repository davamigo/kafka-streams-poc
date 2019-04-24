package com.example.kafka.streams.poc.kafka.config;

import com.example.kafka.streams.poc.schemas.product.Product;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

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
        assertTrue(props.size() > 3);
        assertEquals("101", props.get("bootstrap.servers"));
        assertEquals("102", props.get("acks"));
        assertEquals("103", props.get("retries"));
        assertEquals("104", props.get("linger.ms"));
        assertEquals("105", props.get("schema.registry.url"));
        assertEquals(KafkaAvroSerializer.class, props.get("key.serializer"));
        assertEquals(KafkaAvroSerializer.class, props.get("value.serializer"));
    }

    @Test
    public void testProducerFactory() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        ProducerFactory<String, Product> factory = kafkaConfig.productProducerFactory(new HashMap<>());

        // Assertions
        assertNotNull(factory);
    }

    @Test
    public void testMappedValueKafkaTemplate() {

        // Run the test
        KafkaConfig kafkaConfig = new KafkaConfig(environment);
        KafkaTemplate<String, Product> template = kafkaConfig.productKafkaProducerTemplate();

        // Assertions
        assertNotNull(template);
    }
}
