package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.member.MemberAddress;
import com.example.kafka.streams.poc.service.processor.member.NewMemberReceptionProcessorInterface;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for NewMembersKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestNewMembersKafkaConsumer {

    @Mock
    NewMemberReceptionProcessorInterface newMemberReceptionProcessorInterface;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        Member member = getTestMember();

        // Run the test
        NewMembersKafkaConsumer newMembersKafkaConsumer = new NewMembersKafkaConsumer(newMemberReceptionProcessorInterface);
        newMembersKafkaConsumer.listen(member, ack, "101", "ttt");

        // Assertions
        verify(newMemberReceptionProcessorInterface, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.member.Member.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        Member member = getTestMember();

        Mockito.doThrow(new ProcessorException("_msg_")).when(newMemberReceptionProcessorInterface).process(any());

        // Run the test
        NewMembersKafkaConsumer newMembersKafkaConsumer = new NewMembersKafkaConsumer(newMemberReceptionProcessorInterface);
        newMembersKafkaConsumer.listen(member, ack, "101", "ttt");

        // Assertions
        verify(newMemberReceptionProcessorInterface, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.member.Member.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A member for testing purposes
     */
    private Member getTestMember() {

        List<MemberAddress> addresses = new ArrayList<>();

        return Member
                .newBuilder()
                .setUuid("101")
                .setFirstName("102")
                .setLastName("103")
                .setAddresses(addresses)
                .build();
    }
}
