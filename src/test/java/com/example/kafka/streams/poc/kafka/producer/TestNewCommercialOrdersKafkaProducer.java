package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NewCommercialOrdersKafkaProducer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestNewCommercialOrdersKafkaProducer {

    @Mock
    KafkaTemplate<String, CommercialOrder> commercialOrderKafkaProducerTemplate;

    @Test
    public void testPublishWhenEverythingOk() {

        // Prepare test data
        CommercialOrder commercialOrder = new CommercialOrder("101", 102L, "103", null, null, null);
        String topic = "111";

        ProducerRecord<String, CommercialOrder> producerRecord = new ProducerRecord<>(topic, commercialOrder);
        TopicPartition topicPartition = new TopicPartition(topic, 112);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 213L, 214L, 215L, 216L, 217, 218);
        SendResult<String, CommercialOrder> sendResult = new SendResult<>(producerRecord, recordMetadata);
        SettableListenableFuture<SendResult<String, CommercialOrder>> task = new SettableListenableFuture<>();
        task.set(sendResult);

        when(commercialOrderKafkaProducerTemplate.send(any(ProducerRecord.class))).thenReturn(task);

        // Run the test
        NewCommercialOrdersKafkaProducer newCommercialOrdersKafkaProducer = new NewCommercialOrdersKafkaProducer(commercialOrderKafkaProducerTemplate, topic);
        newCommercialOrdersKafkaProducer.publish(commercialOrder);

        // Assertions
        verify(commercialOrderKafkaProducerTemplate, times(1)).send(any(ProducerRecord.class));
    }

    @Test(expected = KafkaProducerException.class)
    public void testPublishWhenErrorOccurred() {

        // Prepare test data
        CommercialOrder commercialOrder = new CommercialOrder("201", 202L, "203", null, null, null);
        String topic = "211";

        when(commercialOrderKafkaProducerTemplate.send(any(ProducerRecord.class))).thenThrow(new KafkaException("_message_"));

        // Run the test
        NewCommercialOrdersKafkaProducer newCommercialOrdersKafkaProducer = new NewCommercialOrdersKafkaProducer(commercialOrderKafkaProducerTemplate, topic);
        newCommercialOrdersKafkaProducer.publish(commercialOrder);
    }
}
