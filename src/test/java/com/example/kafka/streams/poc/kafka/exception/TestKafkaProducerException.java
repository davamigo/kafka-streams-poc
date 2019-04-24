package com.example.kafka.streams.poc.kafka.exception;

import com.example.kafka.streams.poc.schemas.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for KafkaProducerException
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestKafkaProducerException {

    @Test
    public void testCreateException() {

        Product product = new Product("101", "102", 103f);
        String topic = "201";
        Exception cause = new Exception();

        KafkaProducerException exc = new KafkaProducerException(cause, product, topic);

        assertEquals(product, exc.getProduct());
        assertEquals(topic, exc.getTopic());
    }

    @Test
    public void testErrorMessage() {

        Product product = new Product("101", "102", 103f);
        String topic = "201";
        Exception cause = new Exception();

        KafkaProducerException exc = new KafkaProducerException(cause, product, topic);
        String msg = exc.getMessage();

        assertNotNull(msg);
        assertTrue(msg.contains("101"));
        assertTrue(msg.contains("201"));
    }
}
