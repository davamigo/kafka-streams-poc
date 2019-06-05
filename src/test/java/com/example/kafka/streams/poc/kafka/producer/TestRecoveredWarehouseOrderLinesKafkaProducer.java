package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
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
import static org.mockito.Mockito.when;

/**
 * Unit tests for RecoveredWarehouseOrderLinesKafkaProducer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestRecoveredWarehouseOrderLinesKafkaProducer {

    @Mock
    KafkaTemplate<String, WarehouseOrderLine> warehouseOrderLineKafkaProducerTemplate;

    @Test
    public void testPublishWhenEverythingOk() {

        // Prepare test data
        WarehouseOrderLine warehouseOrderLine = new WarehouseOrderLine("101", "102", 103L, "104", 105, "106", "107", 108);
        String topic = "111";

        ProducerRecord<String, WarehouseOrderLine> producerRecord = new ProducerRecord<>(topic, warehouseOrderLine);
        TopicPartition topicPartition = new TopicPartition(topic, 112);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 213L, 214L, 215L, 216L, 217, 218);
        SendResult<String, WarehouseOrderLine> sendResult = new SendResult<>(producerRecord, recordMetadata);
        SettableListenableFuture<SendResult<String, WarehouseOrderLine>> task = new SettableListenableFuture<>();
        task.set(sendResult);

        when(warehouseOrderLineKafkaProducerTemplate.send(any(ProducerRecord.class))).thenReturn(task);

        // Run the test
        RecoveredWarehouseOrderLinesKafkaProducer recoveredWarehouseOrderLinesKafkaProducer = new RecoveredWarehouseOrderLinesKafkaProducer(warehouseOrderLineKafkaProducerTemplate, topic);
        recoveredWarehouseOrderLinesKafkaProducer.publish(warehouseOrderLine);

        // Assertions
        verify(warehouseOrderLineKafkaProducerTemplate, times(1)).send(any(ProducerRecord.class));
    }

    @Test(expected = KafkaProducerException.class)
    public void testPublishWhenErrorOccurred() {

        // Prepare test data
        WarehouseOrderLine warehouseOrderLine = new WarehouseOrderLine("201", "202", 203L, "204", 205, "206", "207", 208);
        String topic = "211";

        when(warehouseOrderLineKafkaProducerTemplate.send(any(ProducerRecord.class))).thenThrow(new KafkaException("_message_"));

        // Run the test
        RecoveredWarehouseOrderLinesKafkaProducer recoveredWarehouseOrderLinesKafkaProducer = new RecoveredWarehouseOrderLinesKafkaProducer(warehouseOrderLineKafkaProducerTemplate, topic);
        recoveredWarehouseOrderLinesKafkaProducer.publish(warehouseOrderLine);
    }
}
