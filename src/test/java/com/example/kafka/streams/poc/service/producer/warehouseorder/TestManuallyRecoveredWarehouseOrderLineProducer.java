package com.example.kafka.streams.poc.service.producer.warehouseorder;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.kafka.producer.RecoveredWarehouseOrderLinesKafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test ManuallyRecoveredWarehouseOrderLineProducer
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestManuallyRecoveredWarehouseOrderLineProducer {

    @Mock
    private RecoveredWarehouseOrderLinesKafkaProducer recoveredWarehouseOrderLinesKafkaProducer;

    @Test
    public void testPublishWarehouseOrderLineCallsKafkaProducer() {

        WarehouseOrderLine warehouseOrderLine = new WarehouseOrderLine("101", "102", new Date(103L), "104", 105, "106", "107", 108);

        ManuallyRecoveredWarehouseOrderLineProducer service = new ManuallyRecoveredWarehouseOrderLineProducer(recoveredWarehouseOrderLinesKafkaProducer);
        service.publish(warehouseOrderLine);

        verify(recoveredWarehouseOrderLinesKafkaProducer, times(1)).publish(any());
    }
}
