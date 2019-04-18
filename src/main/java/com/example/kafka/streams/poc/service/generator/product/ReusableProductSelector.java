package com.example.kafka.streams.poc.service.generator.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service to reuse random generated products for generating testing data
 */
@Component
public class ReusableProductSelector implements ProductGeneratorInterface {

    /** Service to generate the products with random data */
    private ProductGeneratorInterface productGenerator;

    /** Default max. products in the list */
    private static final int DEFAULT_MAX_PRODUCTS = 100;

    /** List of products created in this session */
    private List<Product> createdProducts;

    /** Max. products allowed in the list */
    private int maxProducts;

    /**
     * Default constructor
     *
     * @param productGenerator the service to generate new products
     */
    @Autowired
    public ReusableProductSelector(RandomProductGenerator productGenerator) {
        this.productGenerator = productGenerator;
        this.createdProducts = new ArrayList<>();
        this.maxProducts = DEFAULT_MAX_PRODUCTS;
    }

    /**
     * Test constructor
     *
     * @param productGenerator the service to generate new products
     * @param maxProducts the max. nenbers allowed in the list
     */
    public ReusableProductSelector(RandomProductGenerator productGenerator, int maxProducts) {
        this.productGenerator = productGenerator;
        this.createdProducts = new ArrayList<>();
        this.maxProducts = maxProducts;
    }

    /**
     * Selects a random product or creates a product
     *
     * @return a random or new product
     */
    @Override
    public synchronized Product getProduct() {

        // Select a random product from the stored products (or null)
        Product product = selectRandomProduct();
        if (product != null) {
            return product;
        }

        // Generate a new product and store it in the list
        product = productGenerator.getProduct();
        createdProducts.add(product);

        // Return a copy of the product
        return product;
    }

    /**
     * Selects a random product or null
     *
     * @return the existing product or null
     */
    private Product selectRandomProduct() {

        // If the list is empty, return null
        if (createdProducts.isEmpty()) {
            return null;
        }

        // Generate a random index between 0 and max-products -1
        int index = ((new Random()).nextInt(maxProducts));

        // If the index is not in the list, return null
        if (index >= createdProducts.size()) {
            return null;
        }

        // Return a copy of the product at this position
        Product sourceProduct = createdProducts.get(index);
        return Product.newBuilder().set(sourceProduct).build();
    }
}
