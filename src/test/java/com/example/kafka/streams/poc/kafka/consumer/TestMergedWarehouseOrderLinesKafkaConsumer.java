package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.warehouse.DefaultMergedWarehouseOrderLineReceptionProcessor;
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
 * Unit tests for MergedWarehouseOrderLinesKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestMergedWarehouseOrderLinesKafkaConsumer {

    @Mock
    DefaultMergedWarehouseOrderLineReceptionProcessor mergedWarehouseOrderLineReceptionProcessorMock;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        WarehouseOrderLine warehouseOrderLine = getTestWarehouseOrderLine();

        // Run the test
        MergedWarehouseOrderLinesKafkaConsumer mergedWarehouseOrderLinesKafkaConsumer = new MergedWarehouseOrderLinesKafkaConsumer(mergedWarehouseOrderLineReceptionProcessorMock);
        mergedWarehouseOrderLinesKafkaConsumer.listen(warehouseOrderLine, ack, "101", "ttt");

        // Assertions
        verify(mergedWarehouseOrderLineReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        WarehouseOrderLine warehouseOrderLine = getTestWarehouseOrderLine();

        Mockito.doThrow(new ProcessorException("_msg_")).when(mergedWarehouseOrderLineReceptionProcessorMock).process(any());

        // Run the test
        MergedWarehouseOrderLinesKafkaConsumer mergedWarehouseOrderLinesKafkaConsumer = new MergedWarehouseOrderLinesKafkaConsumer(mergedWarehouseOrderLineReceptionProcessorMock);
        mergedWarehouseOrderLinesKafkaConsumer.listen(warehouseOrderLine, ack, "101", "ttt");

        // Assertions
        verify(mergedWarehouseOrderLineReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A purchase order for testing purposes
     */
    private WarehouseOrderLine getTestWarehouseOrderLine() {

        return WarehouseOrderLine
                .newBuilder()
                .setUuid("101")
                .setCountry("102")
                .setDate(103L)
                .setProductUuid("104")
                .setProductLegacyId(105)
                .setProductName("106")
                .setProductBarCode("107")
                .setQuantity(108)
                .build();
    }
}
