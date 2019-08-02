package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.mongodb.entity.ProductLegacyIdEntity;
import com.example.kafka.streams.poc.mongodb.repository.ProductLegacyIdRepository;
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
 * Unit test for DefaultProductLegacyIdReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultProductLegacyIdReceivedProcessor {

    @Mock
    private ProductLegacyIdRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultProductLegacyIdReceivedProcessor processor = new DefaultProductLegacyIdReceivedProcessor(repository);
        processor.process("101", 102);

        verify(repository, times(1)).insert(any(ProductLegacyIdEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        when(repository.findById("201")).thenReturn(Optional.of(new ProductLegacyIdEntity("201", 0)));

        DefaultProductLegacyIdReceivedProcessor processor = new DefaultProductLegacyIdReceivedProcessor(repository);
        processor.process("201", 202);

        verify(repository, times(1)).save(any(ProductLegacyIdEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        when(repository.insert(any(ProductLegacyIdEntity.class))).thenThrow(new NullPointerException());

        DefaultProductLegacyIdReceivedProcessor processor = new DefaultProductLegacyIdReceivedProcessor(repository);
        processor.process("301", 302);

        verify(repository, times(1)).insert(any(ProductLegacyIdEntity.class));
    }
}
