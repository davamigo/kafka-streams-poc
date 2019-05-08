package com.example.kafka.streams.poc.service.processor.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.mongodb.entity.MemberEntity;
import com.example.kafka.streams.poc.mongodb.repository.MemberRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for DefaultNewMemberReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultNewMemberReceivedProcessor {

    @Mock
    private MemberRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        Member member = Member.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultNewMemberReceivedProcessor processor = new DefaultNewMemberReceivedProcessor(repository);
        processor.process(member);

        verify(repository, times(1)).insert(any(MemberEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        Member member = Member.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new MemberEntity()));

        DefaultNewMemberReceivedProcessor processor = new DefaultNewMemberReceivedProcessor(repository);
        processor.process(member);

        verify(repository, times(1)).save(any(MemberEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        Member member = Member.newBuilder().setUuid("301").build();

        when(repository.insert(any(MemberEntity.class))).thenThrow(new NullPointerException());

        DefaultNewMemberReceivedProcessor processor = new DefaultNewMemberReceivedProcessor(repository);
        processor.process(member);

        verify(repository, times(1)).insert(any(MemberEntity.class));
    }
}
