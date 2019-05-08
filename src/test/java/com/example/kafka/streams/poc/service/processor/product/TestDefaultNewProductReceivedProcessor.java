package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import com.example.kafka.streams.poc.mongodb.entity.ProductEntity;
import com.example.kafka.streams.poc.mongodb.repository.ProductRepository;
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
 * Unit test for DefaultNewProductReceivedProcessor service
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestDefaultNewProductReceivedProcessor {

    @Mock
    private ProductRepository repository;

    @Test
    public void testProcessCallsInsertWhenNew() {

        Product product = Product.newBuilder().setUuid("101").build();

        when(repository.findById("101")).thenReturn(Optional.empty());

        DefaultNewProductReceivedProcessor processor = new DefaultNewProductReceivedProcessor(repository);
        processor.process(product);

        verify(repository, times(1)).insert(any(ProductEntity.class));
    }

    @Test
    public void testProcessCallsInsertWhenExist() {

        Product product = Product.newBuilder().setUuid("201").build();

        when(repository.findById("201")).thenReturn(Optional.of(new ProductEntity()));

        DefaultNewProductReceivedProcessor processor = new DefaultNewProductReceivedProcessor(repository);
        processor.process(product);

        verify(repository, times(1)).save(any(ProductEntity.class));
    }

    @Test(expected = ProcessorException.class)
    public void testProcessWhenExceptionWritingToRepository() {

        Product product = Product.newBuilder().setUuid("301").build();

        when(repository.insert(any(ProductEntity.class))).thenThrow(new NullPointerException());

        DefaultNewProductReceivedProcessor processor = new DefaultNewProductReceivedProcessor(repository);
        processor.process(product);

        verify(repository, times(1)).insert(any(ProductEntity.class));
    }
}
