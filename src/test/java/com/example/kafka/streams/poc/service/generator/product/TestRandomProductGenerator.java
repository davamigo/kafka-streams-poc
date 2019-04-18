package com.example.kafka.streams.poc.service.generator.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(product.getPrice() > 0.00f);
    }
}
