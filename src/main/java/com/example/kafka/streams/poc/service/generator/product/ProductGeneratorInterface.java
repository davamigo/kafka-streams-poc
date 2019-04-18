package com.example.kafka.streams.poc.service.generator.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;

/**
 * Interface to a service to get a product for generating testing data
 */
public interface ProductGeneratorInterface {

    /**
     * Get a product
     *
     * @return a product
     */
    Product getProduct();
}
