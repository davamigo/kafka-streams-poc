package com.example.kafka.streams.poc.service.generator.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

/**
 * Service to get a product for generating testing data
 */
@Component
public class RandomProductGenerator implements ProductGeneratorInterface {

    /** Array of product colors */
    private static final String[] productColors = {
            "Black",
            "Red",
            "Green",
            "Blue",
            "Yellow",
            "Brown",
            "Orange",
            "Pink",
            "Cyan",
            "Magenta",
            "Grey",
            "White"
    };

    /** Array of product adjectives */
    private static final String[] productAdjectives = {
            "Thin",
            "Thick",
            "Bold",
            "Heavy"
    };

    /** Array of product names */
    private static final String[] productBaseNames = {
            "Pen",
            "Pencil",
            "Crayon",
            "Marker"
    };

    /**
     * Default constructor
     */
    public RandomProductGenerator() {
    }

    /**
     * Generate a product with random data
     *
     * @return a product with random data
     */
    @Override
    public synchronized Product getProduct() {
        Product.Builder builder = Product.newBuilder();
        builder.setUuid(UUID.randomUUID().toString());
        builder.setName(selectRandomProductName());
        builder.setPrice(selectRandomProductPrice());

        return builder.build();
    }

    /**
     * Selects a random product name using defined color, adjective and product base
     *
     * @return a random product name
     */
    private String selectRandomProductName() {

        String color = productColors[(new Random()).nextInt(productColors.length)];
        String adjective = productAdjectives[(new Random()).nextInt(productAdjectives.length)];
        String baseName = productBaseNames[(new Random()).nextInt(productBaseNames.length)];

        return color + " " + adjective + " " + baseName;
    }

    /**
     * Selects a random product price between 0 and 99.99
     *
     * @return a random product price
     */
    private float selectRandomProductPrice() {
        return ((float)(new Random()).nextInt(10000)) / 100;
    }
}
