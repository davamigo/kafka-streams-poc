package com.example.kafka.streams.poc.service.generator.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for RandomProductGenerator service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestRandomProductGenerator {

    @Test
    public void testGetProductReturnProductWithRandomData() {

        RandomProductGenerator service = new RandomProductGenerator();
        Product product = service.getProduct();

        assertNotNull(product);
        assertNotNull(product.getUuid());
        assertNotNull(product.getName());
        assertNotNull(product.getType());
        assertNotNull(product.getBarCode());
        assertEquals(13, product.getBarCode().length());
        assertTrue(product.getPrice() >= 0.00f);
        assertTrue(product.getPrice() <= 50.00f);
    }
}
