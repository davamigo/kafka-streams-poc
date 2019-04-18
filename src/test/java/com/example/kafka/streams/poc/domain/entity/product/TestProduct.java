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
        assertTrue(product.getPrice() < 0.01f);
        assertTrue(product.getPrice() > -0.01f);
    }

    @Test
    public void testCompleteConstructor() {
        Product product = new Product("101", "102", 103f);

        assertEquals("101", product.getUuid());
        assertEquals("102", product.getName());
        assertTrue(product.getPrice() < 103.01f);
        assertTrue(product.getPrice() > 102.99f);
    }

    @Test
    public void testTwoProductsAreEqualsWhenTheyHaveTheSameUuid() {

        Product product1 = new Product("201", "202", 203f);
        Product product2 = new Product("201", "204", 205f);

        assertEquals(product1, product2);
        assertNotSame(product1, product2);
    }

    @Test
    public void testTwoProductsAreDifferentWhenTheyHaveDifferentUuid() {
        Product product1 = new Product("301", "302", 303f);
        Product product2 = new Product("401", "302", 303f);

        assertNotEquals(product1, product2);
    }

    @Test
    public void testEqualsWithNonProduct() {
        Product product = new Product();

        assertNotEquals(product, new String());
    }
}
