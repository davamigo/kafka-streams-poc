package com.example.kafka.streams.poc.domain.entity.product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for Product domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestProduct {

    @Test
    public void testDefaultConstructor() {
        Product product = new Product();

        assertNull(product.getUuid());
        assertNull(product.getName());
        assertNull(product.getType());
        assertNull(product.getBarCode());
        assertEquals(0f, product.getPrice(), 0.001);
    }

    @Test
    public void testCompleteConstructor() {
        Product product = new Product("101", "102", "103", "104", 105f);

        assertEquals("101", product.getUuid());
        assertEquals("102", product.getName());
        assertEquals("103", product.getType());
        assertEquals("104", product.getBarCode());
        assertEquals(105f, product.getPrice(), 0.001);
    }

    @Test
    public void testTwoProductsAreEqualsWhenTheyHaveTheSameUuid() {

        Product product1 = new Product("201", "202", "203", "204", 205f);
        Product product2 = new Product("201", "212", "213", "214", 215f);

        assertEquals(product1, product2);
        assertNotSame(product1, product2);
    }

    @Test
    public void testTwoProductsAreDifferentWhenTheyHaveDifferentUuid() {
        Product product1 = new Product("301", "302", "303", "304", 305f);
        Product product2 = new Product("311", "302", "303", "304", 305f);

        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithNonProduct() {
        Product product = new Product();

        assertNotEquals(product, new String());
    }
}
