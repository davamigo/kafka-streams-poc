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
                .setType("103")
                .setBarCode("104")
                .setPrice(105f)
                .build();

        assertEquals("101", product.getUuid());
        assertEquals("102", product.getName());
        assertEquals("103", product.getType());
        assertEquals("104", product.getBarCode());
        assertEquals(105f, product.getPrice(), 0.001);
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        Product source = new Product("201", "202", "203", "204", 205f);

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
                        .setType("303")
                        .setBarCode("304")
                        .setPrice(305f)
                        .build();

        Product product = Product.newBuilder().set(sourceProduct).build();

        assertEquals("301", product.getUuid());
        assertEquals("302", product.getName());
        assertEquals("303", product.getType());
        assertEquals("304", product.getBarCode());
        assertEquals(305f, product.getPrice(), 0.001);
    }
}
