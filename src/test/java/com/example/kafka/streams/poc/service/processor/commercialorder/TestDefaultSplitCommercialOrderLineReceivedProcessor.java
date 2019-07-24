package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderLineSplitEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderLineSplitRepository;
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
 * Unit test for DefaultSplitCommercialOrderLineReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultSplitCommercialOrderLineReceivedProcessor {

    @Mock
    private CommercialOrderLineSplitRepository repository;

    @Test
    public void testProcessCallsRepository() {

        CommercialOrderLineSplit order = CommercialOrderLineSplit.newBuilder().build();

        DefaultSplitCommercialOrderLineReceivedProcessor processor = new DefaultSplitCommercialOrderLineReceivedProcessor(repository);
        processor.process(order);

        Mockito.verify(repository, Mockito.times(1)).insert(any(CommercialOrderLineSplitEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        Mockito.when(repository.insert(any(CommercialOrderLineSplitEntity.class))).thenThrow(new NullPointerException());

        CommercialOrderLineSplit order = CommercialOrderLineSplit.newBuilder().build();

        DefaultSplitCommercialOrderLineReceivedProcessor processor = new DefaultSplitCommercialOrderLineReceivedProcessor(repository);
        processor.process(order);

        Mockito.verify(repository, Mockito.times(1)).insert(any(CommercialOrderLineSplitEntity.class));
    }
}
