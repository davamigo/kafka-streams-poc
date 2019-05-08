package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a new Product
 */
public interface NewProductReceptionProcessorInterface {

    /**
     * Process the reception of a product
     *
     * @param product the product received
     * @throws ProcessorException when an error occurred
     */
    void process(Product product) throws ProcessorException;
}
