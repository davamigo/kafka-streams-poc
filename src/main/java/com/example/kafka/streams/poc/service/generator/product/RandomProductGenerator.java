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
    private static final String[] PRODUCT_COLORS = {
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
    private static final String[] PRODUCT_ADJECTIVES = {
            "Thin",
            "Thick",
            "Bold",
            "Heavy"
    };

    /** Array of product names */
    private static final String[] PRODUCT_BASE_NAMES = {
            "Pen",
            "Pencil",
            "Crayon",
            "Marker"
    };

    /** The default product type */
    private static final String DEFAULT_TYPE = "Pen";

    /** The maximum price of a product * 100 (meaning 5000 => 50.00) */
    private static int MAX_PRICE = 5000;

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
        builder.setType(selectRandomType());
        builder.setBarCode(selectRandomBarCode());
        builder.setPrice(selectRandomProductPrice());

        return builder.build();
    }

    /**
     * Selects a random product name using defined color, adjective and product base
     *
     * @return a random product name
     */
    private String selectRandomProductName() {

        String color = PRODUCT_COLORS[(new Random()).nextInt(PRODUCT_COLORS.length)];
        String adjective = PRODUCT_ADJECTIVES[(new Random()).nextInt(PRODUCT_ADJECTIVES.length)];
        String baseName = PRODUCT_BASE_NAMES[(new Random()).nextInt(PRODUCT_BASE_NAMES.length)];

        return color + " " + adjective + " " + baseName;
    }

    /**
     * Returns a random product type. For this example the type is always "Pen"
     *
     * @return a random product type
     */
    private String selectRandomType() {
        return DEFAULT_TYPE;
    }

    /**
     * Returns a random bar code
     *
     * @return a random bar code
     */
    private String selectRandomBarCode() {

        StringBuilder builder = new StringBuilder();
        int odds = 0;;
        int evens = 0;

        for (int i = 0; i < 12; i++) {
            int digit = (new Random()).nextInt(10);
            if (i % 2 == 0) {
                evens += digit;
            } else {
                odds += digit;
            }
            builder.append(digit);
        }

        int checksum = ((3 * odds) + evens) % 10;
        if (checksum > 0) {
            checksum = 10 - checksum;
        }
        builder.append(checksum);

        return builder.toString();
    }

    /**
     * Selects a random product price between 0 and 99.99
     *
     * @return a random product price
     */
    private float selectRandomProductPrice() {
        return ((float)(new Random()).nextInt(MAX_PRICE)) / 100;
    }
}
