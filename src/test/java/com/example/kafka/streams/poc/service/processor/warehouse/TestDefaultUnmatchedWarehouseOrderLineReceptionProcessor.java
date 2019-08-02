package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineUnmatchedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineUnmatchedRepository;
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
 * Unit test for DefaultUnmatchedWarehouseOrderLineReceptionProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultUnmatchedWarehouseOrderLineReceptionProcessor {

    @Mock
    private WarehouseOrderLineUnmatchedRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultUnmatchedWarehouseOrderLineReceptionProcessor processor = new DefaultUnmatchedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).insert(any(WarehouseOrderLineUnmatchedEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new WarehouseOrderLineUnmatchedEntity()));

        DefaultUnmatchedWarehouseOrderLineReceptionProcessor processor = new DefaultUnmatchedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).save(any(WarehouseOrderLineUnmatchedEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("301").build();

        when(repository.insert(any(WarehouseOrderLineUnmatchedEntity.class))).thenThrow(new NullPointerException());

        DefaultUnmatchedWarehouseOrderLineReceptionProcessor processor = new DefaultUnmatchedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).insert(any(WarehouseOrderLineUnmatchedEntity.class));
    }
}
