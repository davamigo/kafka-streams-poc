package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for product mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestProductEntity {

    @Test
    public void testEmptyConstructor() {

        ProductEntity productEntity = new ProductEntity();

        assertNull(productEntity.getUuid());
        assertNull(productEntity.getName());
        assertNull(productEntity.getType());
        assertNull(productEntity.getBarCode());
        assertEquals(0f, productEntity.getPrice(), 0.001);
    }

    @Test
    public void testCopyConstructor() {

        Product product = Product
                .newBuilder()
                .setUuid("101")
                .setName("102")
                .setType("103")
                .setBarCode("104")
                .setPrice(105f)
                .build();

        ProductEntity productEntity = new ProductEntity(product);

        assertEquals("101", productEntity.getUuid());
        assertEquals("102", productEntity.getName());
        assertEquals("103", productEntity.getType());
        assertEquals("104", productEntity.getBarCode());
        assertEquals(105f, productEntity.getPrice(), 0.001);
    }
}
