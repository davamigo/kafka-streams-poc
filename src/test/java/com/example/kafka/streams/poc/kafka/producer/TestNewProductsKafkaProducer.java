package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.product.Product;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NewProductsKafkaProducer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestNewProductsKafkaProducer {

    @Mock
    KafkaTemplate<String, Product> productKafkaProducerTemplate;

    @Test
    public void testPublishWhenEverythingOk() {

        // Prepare test data
        Product product = new Product("101", "102", "103", "104", 105f);
        String topic = "111";

        ProducerRecord<String, Product> producerRecord = new ProducerRecord<>(topic, product);
        TopicPartition topicPartition = new TopicPartition(topic, 112);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 213L, 214L, 215L, 216L, 217, 218);
        SendResult<String, Product> sendResult = new SendResult<>(producerRecord, recordMetadata);
        SettableListenableFuture<SendResult<String, Product>> task = new SettableListenableFuture<>();
        task.set(sendResult);

        when(productKafkaProducerTemplate.send(any(ProducerRecord.class))).thenReturn(task);

        // Run the test
        NewProductsKafkaProducer newProductsKafkaProducer = new NewProductsKafkaProducer(productKafkaProducerTemplate, topic);
        newProductsKafkaProducer.publish(product);

        // Assertions
        verify(productKafkaProducerTemplate, times(1)).send(any(ProducerRecord.class));
    }

    @Test(expected = KafkaProducerException.class)
    public void testPublishWhenErrorOccurred() {

        // Prepare test data
        Product product = new Product("201", "202", "203", "204", 205f);
        String topic = "211";

        when(productKafkaProducerTemplate.send(any(ProducerRecord.class))).thenThrow(new KafkaException("_message_"));

        // Run the test
        NewProductsKafkaProducer newProductsKafkaProducer = new NewProductsKafkaProducer(productKafkaProducerTemplate, topic);
        newProductsKafkaProducer.publish(product);
    }
}
