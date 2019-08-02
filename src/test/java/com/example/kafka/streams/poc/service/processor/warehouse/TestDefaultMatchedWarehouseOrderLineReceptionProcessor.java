package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineMatchedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineMatchedRepository;
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
 * Unit test for DefaultMatchedWarehouseOrderLineReceptionProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultMatchedWarehouseOrderLineReceptionProcessor {

    @Mock
    private WarehouseOrderLineMatchedRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultMatchedWarehouseOrderLineReceptionProcessor processor = new DefaultMatchedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).insert(any(WarehouseOrderLineMatchedEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new WarehouseOrderLineMatchedEntity()));

        DefaultMatchedWarehouseOrderLineReceptionProcessor processor = new DefaultMatchedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).save(any(WarehouseOrderLineMatchedEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("301").build();

        when(repository.insert(any(WarehouseOrderLineMatchedEntity.class))).thenThrow(new NullPointerException());

        DefaultMatchedWarehouseOrderLineReceptionProcessor processor = new DefaultMatchedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).insert(any(WarehouseOrderLineMatchedEntity.class));
    }
}
