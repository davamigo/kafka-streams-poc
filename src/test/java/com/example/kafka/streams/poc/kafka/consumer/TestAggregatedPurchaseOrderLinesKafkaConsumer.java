package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.purchaseorder.AggregatedPurchaseOrderLineReceptionProcessorInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for AggregatedPurchaseOrderLinesKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestAggregatedPurchaseOrderLinesKafkaConsumer {

    @Mock
    AggregatedPurchaseOrderLineReceptionProcessorInterface aggregatedPurchaseOrderLineReceptionProcessorMock;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        PurchaseOrderLine purchaseOrderLine = getTestPurchaseOrderLine();

        // Run the test
        AggregatedPurchaseOrderLinesKafkaConsumer aggregatedPurchaseOrderLinesKafkaConsumer = new AggregatedPurchaseOrderLinesKafkaConsumer(aggregatedPurchaseOrderLineReceptionProcessorMock);
        aggregatedPurchaseOrderLinesKafkaConsumer.listen(purchaseOrderLine, ack, "101", "ttt");

        // Assertions
        verify(aggregatedPurchaseOrderLineReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        PurchaseOrderLine purchaseOrder = getTestPurchaseOrderLine();

        Mockito.doThrow(new ProcessorException("_msg_")).when(aggregatedPurchaseOrderLineReceptionProcessorMock).process(any());

        // Run the test
        AggregatedPurchaseOrderLinesKafkaConsumer aggregatedPurchaseOrderLinesKafkaConsumer = new AggregatedPurchaseOrderLinesKafkaConsumer(aggregatedPurchaseOrderLineReceptionProcessorMock);
        aggregatedPurchaseOrderLinesKafkaConsumer.listen(purchaseOrder, ack, "101", "ttt");

        // Assertions
        verify(aggregatedPurchaseOrderLineReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A purchase order line for testing purposes
     */
    private PurchaseOrderLine getTestPurchaseOrderLine() {

        return PurchaseOrderLine
                .newBuilder()
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(104L)
                .setProductUuid("105")
                .setProductName("106")
                .setProductType("107")
                .setProductBarCode("108")
                .setProductPrice(109f)
                .setQuantity(110)
                .build();
    }
}
