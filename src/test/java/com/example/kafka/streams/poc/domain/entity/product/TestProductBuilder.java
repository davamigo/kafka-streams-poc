package com.example.kafka.streams.poc.domain.entity.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for Product.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestProductBuilder {

    @Test
    public void testBuiderSettersReturnExpectedResults() {

        Product.Builder builder = Product.newBuilder();
        Product product = builder
                .setUuid("101")
                .setName("102")
                .setPrice(103.0f)
                .build();

        assertEquals("101", product.getUuid());
        assertEquals("102", product.getName());
        assertTrue(product.getPrice() < 103.01f);
        assertTrue(product.getPrice() > 102.99f);
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        Product source = new Product("201", "202", 203f);

        Product.Builder builder = Product.newBuilder().set(source);
        Product product = builder.build();

        assertEquals(source, product);
        assertNotSame(source, product);
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.product.Product sourceProduct =
                com.example.kafka.streams.poc.schemas.product.Product.newBuilder()
                        .setUuid("301")
                        .setName("302")
                        .setPrice(303.0f)
                        .build();

        Product product = Product.newBuilder().set(sourceProduct).build();

        assertEquals("301", product.getUuid());
        assertEquals("302", product.getName());
        assertTrue(product.getPrice() < 303.01f);
        assertTrue(product.getPrice() > 302.99f);
    }
}
