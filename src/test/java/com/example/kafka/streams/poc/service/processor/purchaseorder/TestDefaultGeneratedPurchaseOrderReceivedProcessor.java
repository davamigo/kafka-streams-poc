package com.example.kafka.streams.poc.service.processor.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder;
import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderRepository;
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
 * Unit test for DefaultGeneratedPurchaseOrderReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultGeneratedPurchaseOrderReceivedProcessor {

    @Mock
    private PurchaseOrderRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        PurchaseOrder purchaseOrder = PurchaseOrder.newBuilder().setKey("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultGeneratedPurchaseOrderReceivedProcessor processor = new DefaultGeneratedPurchaseOrderReceivedProcessor(repository);
        processor.process(purchaseOrder);

        verify(repository, times(1)).insert(any(PurchaseOrderEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        PurchaseOrder purchaseOrder = PurchaseOrder.newBuilder().setKey("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new PurchaseOrderEntity()));

        DefaultGeneratedPurchaseOrderReceivedProcessor processor = new DefaultGeneratedPurchaseOrderReceivedProcessor(repository);
        processor.process(purchaseOrder);

        verify(repository, times(1)).save(any(PurchaseOrderEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        PurchaseOrder purchaseOrder = PurchaseOrder.newBuilder().setKey("301").build();

        when(repository.insert(any(PurchaseOrderEntity.class))).thenThrow(new NullPointerException());

        DefaultGeneratedPurchaseOrderReceivedProcessor processor = new DefaultGeneratedPurchaseOrderReceivedProcessor(repository);
        processor.process(purchaseOrder);

        verify(repository, times(1)).insert(any(PurchaseOrderEntity.class));
    }
}
