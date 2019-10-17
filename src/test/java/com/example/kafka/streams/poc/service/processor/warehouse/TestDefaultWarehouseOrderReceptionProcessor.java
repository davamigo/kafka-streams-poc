package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderRepository;
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
 * Unit test for DefaultWarehouseOrderReceptionProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultWarehouseOrderReceptionProcessor {

    @Mock
    private WarehouseOrderRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        WarehouseOrder warehouseOrder = WarehouseOrder.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultWarehouseOrderReceptionProcessor processor = new DefaultWarehouseOrderReceptionProcessor(repository);
        processor.process(warehouseOrder);

        verify(repository, times(1)).insert(any(WarehouseOrderEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        WarehouseOrder warehouseOrder = WarehouseOrder.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new WarehouseOrderEntity()));

        DefaultWarehouseOrderReceptionProcessor processor = new DefaultWarehouseOrderReceptionProcessor(repository);
        processor.process(warehouseOrder);

        verify(repository, times(1)).save(any(WarehouseOrderEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        WarehouseOrder warehouseOrder = WarehouseOrder.newBuilder().setUuid("301").build();

        when(repository.insert(any(WarehouseOrderEntity.class))).thenThrow(new NullPointerException());

        DefaultWarehouseOrderReceptionProcessor processor = new DefaultWarehouseOrderReceptionProcessor(repository);
        processor.process(warehouseOrder);

        verify(repository, times(1)).insert(any(WarehouseOrderEntity.class));
    }
}
