package com.example.kafka.streams.poc.service.processor.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderLineEntity;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderLineRepository;
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
 * Unit test for DefaultGeneratedPurchaseOrderLineReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultAggregatedPurchaseOrderLineReceivedProcessor {

    @Mock
    private PurchaseOrderLineRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultAggregatedPurchaseOrderLineReceivedProcessor processor = new DefaultAggregatedPurchaseOrderLineReceivedProcessor(repository);
        processor.process(purchaseOrderLine);

        verify(repository, times(1)).insert(any(PurchaseOrderLineEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new PurchaseOrderLineEntity()));

        DefaultAggregatedPurchaseOrderLineReceivedProcessor processor = new DefaultAggregatedPurchaseOrderLineReceivedProcessor(repository);
        processor.process(purchaseOrderLine);

        verify(repository, times(1)).save(any(PurchaseOrderLineEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        PurchaseOrderLine purchaseOrderLine = PurchaseOrderLine.newBuilder().setUuid("301").build();

        when(repository.insert(any(PurchaseOrderLineEntity.class))).thenThrow(new NullPointerException());

        DefaultAggregatedPurchaseOrderLineReceivedProcessor processor = new DefaultAggregatedPurchaseOrderLineReceivedProcessor(repository);
        processor.process(purchaseOrderLine);

        verify(repository, times(1)).insert(any(PurchaseOrderLineEntity.class));
    }
}
