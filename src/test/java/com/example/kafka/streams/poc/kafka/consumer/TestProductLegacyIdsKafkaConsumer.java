package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.product.ProductLegacyIdReceptionProcessorInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for ProductLegacyIdsKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestProductLegacyIdsKafkaConsumer {

    @Mock
    ProductLegacyIdReceptionProcessorInterface processor;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Run the test
        ProductLegacyIdsKafkaConsumer consumer = new ProductLegacyIdsKafkaConsumer(processor);
        consumer.listen(102, ack, "101", "ttt");

        // Assertions
        verify(processor, times(1)).process(any(String.class), any(Integer.class));
        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        Mockito.doThrow(new ProcessorException("_msg_")).when(processor).process(any(String.class), any(Integer.class));

        // Run the test
        ProductLegacyIdsKafkaConsumer consumer = new ProductLegacyIdsKafkaConsumer(processor);
        consumer.listen(201, ack, "201", "ttt");

        // Assertions
        verify(processor, times(1)).process(any(String.class), any(Integer.class));
        verify(ack, times(0)).acknowledge();
    }
}
