package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder;
import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed;
import com.example.kafka.streams.poc.service.processor.purchaseorder.GeneratedPurchaseOrderReceptionProcessorInterface;
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
 * Unit tests for GeneratedPurchaseOrdersKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestGeneratedPurchaseOrdersKafkaConsumer {

    @Mock
    GeneratedPurchaseOrderReceptionProcessorInterface generatedPurchaseOrderReceptionProcessorMock;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        PurchaseOrder purchaseOrder = getTestPurchaseOrder();

        // Run the test
        GeneratedPurchaseOrdersKafkaConsumer generatedPurchaseOrdersKafkaConsumer = new GeneratedPurchaseOrdersKafkaConsumer(generatedPurchaseOrderReceptionProcessorMock);
        generatedPurchaseOrdersKafkaConsumer.listen(purchaseOrder, ack, "101", "ttt");

        // Assertions
        verify(generatedPurchaseOrderReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        PurchaseOrder purchaseOrder = getTestPurchaseOrder();

        Mockito.doThrow(new ProcessorException("_msg_")).when(generatedPurchaseOrderReceptionProcessorMock).process(any());

        // Run the test
        GeneratedPurchaseOrdersKafkaConsumer generatedPurchaseOrdersKafkaConsumer = new GeneratedPurchaseOrdersKafkaConsumer(generatedPurchaseOrderReceptionProcessorMock);
        generatedPurchaseOrdersKafkaConsumer.listen(purchaseOrder, ack, "101", "ttt");

        // Assertions
        verify(generatedPurchaseOrderReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A purchase order for testing purposes
     */
    private PurchaseOrder getTestPurchaseOrder() {

        List<PurchaseOrderLineCondensed> lines = new ArrayList<>();

        return PurchaseOrder
                .newBuilder()
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(104)
                .setTotalAmount(105f)
                .setTotalQuantity(106)
                .setLines(lines)
                .build();
    }
}
