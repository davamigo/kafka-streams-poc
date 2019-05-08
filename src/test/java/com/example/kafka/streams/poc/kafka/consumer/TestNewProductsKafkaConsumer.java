package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.product.Product;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.product.NewProductReceptionProcessorInterface;
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
 * Unit tests for NewProductsKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestNewProductsKafkaConsumer {

    @Mock
    NewProductReceptionProcessorInterface newProductReceptionProcessorInterface;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        Product product = getTestProduct();

        // Run the test
        NewProductsKafkaConsumer newProductsKafkaConsumer = new NewProductsKafkaConsumer(newProductReceptionProcessorInterface);
        newProductsKafkaConsumer.listen(product, ack, "101", "ttt");

        // Assertions
        verify(newProductReceptionProcessorInterface, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.product.Product.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        Product product = getTestProduct();

        Mockito.doThrow(new ProcessorException("_msg_")).when(newProductReceptionProcessorInterface).process(any());

        // Run the test
        NewProductsKafkaConsumer newProductsKafkaConsumer = new NewProductsKafkaConsumer(newProductReceptionProcessorInterface);
        newProductsKafkaConsumer.listen(product, ack, "101", "ttt");

        // Assertions
        verify(newProductReceptionProcessorInterface, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.product.Product.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A product for testing purposes
     */
    private Product getTestProduct() {

        return Product
                .newBuilder()
                .setUuid("101")
                .setName("102")
                .setType("103")
                .setBarCode("104")
                .setPrice(105f)
                .build();
    }
}
