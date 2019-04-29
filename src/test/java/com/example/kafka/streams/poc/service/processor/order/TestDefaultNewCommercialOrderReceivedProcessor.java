package com.example.kafka.streams.poc.service.processor.order;

import com.example.kafka.streams.poc.domain.entity.order.CommercialOrder;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderRepository;
import com.example.kafka.streams.poc.service.processor.ProcessorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for DefaultNewCommercialOrderReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultNewCommercialOrderReceivedProcessor {

    @Mock
    private CommercialOrderRepository repository;

    @Test
    public void testProcessCallsRepository() {

        CommercialOrder commercialOrder = CommercialOrder.newBuilder().build();

        DefaultNewCommercialOrderReceivedProcessor processor = new DefaultNewCommercialOrderReceivedProcessor(repository);
        processor.process(commercialOrder);

        verify(repository, times(1)).insert(any(CommercialOrderEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        when(repository.insert(any(CommercialOrderEntity.class))).thenThrow(new NullPointerException());

        CommercialOrder commercialOrder = CommercialOrder.newBuilder().build();

        DefaultNewCommercialOrderReceivedProcessor processor = new DefaultNewCommercialOrderReceivedProcessor(repository);
        processor.process(commercialOrder);

        verify(repository, times(1)).insert(any(CommercialOrderEntity.class));
    }
}
