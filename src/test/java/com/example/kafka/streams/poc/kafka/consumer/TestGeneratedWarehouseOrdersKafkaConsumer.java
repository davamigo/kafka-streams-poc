package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.warehouse.DefaultWarehouseOrderReceptionProcessor;
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
 * Unit tests for MergedWarehouseOrdersKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestGeneratedWarehouseOrdersKafkaConsumer {

    @Mock
    DefaultWarehouseOrderReceptionProcessor mergedWarehouseOrderReceptionProcessorMock;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        WarehouseOrder warehouseOrder = getTestWarehouseOrder();

        // Run the test
        GeneratedWarehouseOrdersKafkaConsumer generatedWarehouseOrdersKafkaConsumer = new GeneratedWarehouseOrdersKafkaConsumer(mergedWarehouseOrderReceptionProcessorMock);
        generatedWarehouseOrdersKafkaConsumer.listen(warehouseOrder, ack, "101", "ttt");

        // Assertions
        verify(mergedWarehouseOrderReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        WarehouseOrder warehouseOrder = getTestWarehouseOrder();

        Mockito.doThrow(new ProcessorException("_msg_")).when(mergedWarehouseOrderReceptionProcessorMock).process(any());

        // Run the test
        GeneratedWarehouseOrdersKafkaConsumer generatedWarehouseOrdersKafkaConsumer = new GeneratedWarehouseOrdersKafkaConsumer(mergedWarehouseOrderReceptionProcessorMock);
        generatedWarehouseOrdersKafkaConsumer.listen(warehouseOrder, ack, "101", "ttt");

        // Assertions
        verify(mergedWarehouseOrderReceptionProcessorMock, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A purchase order for testing purposes
     */
    private WarehouseOrder getTestWarehouseOrder() {

        List<WarehouseOrderLineCondensed> lines = new ArrayList<>();
        return WarehouseOrder
                .newBuilder()
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(104L)
                .setLines(lines)
                .build();
    }
}
