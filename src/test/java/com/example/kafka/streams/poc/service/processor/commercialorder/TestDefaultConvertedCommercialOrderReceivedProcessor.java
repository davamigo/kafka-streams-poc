package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderConvertedEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderConvertedRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;

/**
 * Unit test for DefaultConvertedCommercialOrderReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultConvertedCommercialOrderReceivedProcessor {

    @Mock
    private CommercialOrderConvertedRepository repository;

    @Test
    public void testProcessCallsRepository() {

        CommercialOrderConverted order = CommercialOrderConverted.newBuilder().build();

        DefaultConvertedCommercialOrderReceivedProcessor processor = new DefaultConvertedCommercialOrderReceivedProcessor(repository);
        processor.process(order);

        Mockito.verify(repository, Mockito.times(1)).insert(any(CommercialOrderConvertedEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        Mockito.when(repository.insert(any(CommercialOrderConvertedEntity.class))).thenThrow(new NullPointerException());

        CommercialOrderConverted order = CommercialOrderConverted.newBuilder().build();

        DefaultConvertedCommercialOrderReceivedProcessor processor = new DefaultConvertedCommercialOrderReceivedProcessor(repository);
        processor.process(order);

        Mockito.verify(repository, Mockito.times(1)).insert(any(CommercialOrderConvertedEntity.class));
    }
}
