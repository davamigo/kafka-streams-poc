package com.example.kafka.streams.poc.service.generator.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for RandomProductSelector service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestReusableProductSelector {

    @Test
    public void testRandomProductSelectorReturnAProduct() {

        RandomProductGenerator productGenerator = new RandomProductGenerator();
        ReusableProductSelector productSelector = new ReusableProductSelector(productGenerator);

        Product product = productSelector.getProduct();

        assertNotNull(product);
        assertNotNull(product.getUuid());
        assertNotNull(product.getName());
        assertTrue(product.getPrice() >= 0);
        assertTrue(product.getPrice() < 100);
    }

    @Test
    public void testRandomProductSelectorReturnTheSameProductWhenMaxProductIs1() {

        RandomProductGenerator productGenerator = new RandomProductGenerator();
        ReusableProductSelector productSelector = new ReusableProductSelector(productGenerator, 1);

        Product product1 = productSelector.getProduct();
        Product product2 = productSelector.getProduct();

        assertEquals(product1.getUuid(), product2.getUuid());
    }

    @Test
    public void testRandomProductSelectorReturnTheDifferentProductWhenMaxProductIsBig() {

        RandomProductGenerator productGenerator = new RandomProductGenerator();
        ReusableProductSelector productSelector = new ReusableProductSelector(productGenerator, Integer.MAX_VALUE);

        Product product1 = productSelector.getProduct();
        Product product2 = productSelector.getProduct();

        assertNotEquals(product1.getUuid(), product2.getUuid());
    }
}
