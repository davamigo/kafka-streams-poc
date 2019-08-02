package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineMergedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineMergedRepository;
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
 * Unit test for DefaultMergedWarehouseOrderLineReceptionProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultMergedWarehouseOrderLineReceptionProcessor {

    @Mock
    private WarehouseOrderLineMergedRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultMergedWarehouseOrderLineReceptionProcessor processor = new DefaultMergedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).insert(any(WarehouseOrderLineMergedEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new WarehouseOrderLineMergedEntity()));

        DefaultMergedWarehouseOrderLineReceptionProcessor processor = new DefaultMergedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).save(any(WarehouseOrderLineMergedEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        WarehouseOrderLine warehouseOrderLine = WarehouseOrderLine.newBuilder().setUuid("301").build();

        when(repository.insert(any(WarehouseOrderLineMergedEntity.class))).thenThrow(new NullPointerException());

        DefaultMergedWarehouseOrderLineReceptionProcessor processor = new DefaultMergedWarehouseOrderLineReceptionProcessor(repository);
        processor.process(warehouseOrderLine);

        verify(repository, times(1)).insert(any(WarehouseOrderLineMergedEntity.class));
    }
}
