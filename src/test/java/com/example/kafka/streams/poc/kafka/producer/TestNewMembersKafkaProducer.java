package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.member.Member;
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
 * Unit tests for NewMembersKafkaProducer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestNewMembersKafkaProducer {

    @Mock
    KafkaTemplate<String, Member> memberKafkaProducerTemplate;

    @Test
    public void testPublishWhenEverythingOk() {

        // Prepare test data
        Member member = new Member("101", "102", "103", null);
        String topic = "111";

        ProducerRecord<String, Member> producerRecord = new ProducerRecord<>(topic, member);
        TopicPartition topicPartition = new TopicPartition(topic, 112);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 213L, 214L, 215L, 216L, 217, 218);
        SendResult<String, Member> sendResult = new SendResult<>(producerRecord, recordMetadata);
        SettableListenableFuture<SendResult<String, Member>> task = new SettableListenableFuture<>();
        task.set(sendResult);

        when(memberKafkaProducerTemplate.send(any(ProducerRecord.class))).thenReturn(task);

        // Run the test
        NewMembersKafkaProducer newMembersKafkaProducer = new NewMembersKafkaProducer(memberKafkaProducerTemplate, topic);
        newMembersKafkaProducer.publish(member);

        // Assertions
        verify(memberKafkaProducerTemplate, times(1)).send(any(ProducerRecord.class));
    }

    @Test(expected = KafkaProducerException.class)
    public void testPublishWhenErrorOccurred() {

        // Prepare test data
        Member member = new Member("201", "202", "203", null);
        String topic = "211";

        when(memberKafkaProducerTemplate.send(any(ProducerRecord.class))).thenThrow(new KafkaException("_message_"));

        // Run the test
        NewMembersKafkaProducer newMembersKafkaProducer = new NewMembersKafkaProducer(memberKafkaProducerTemplate, topic);
        newMembersKafkaProducer.publish(member);
    }
}
