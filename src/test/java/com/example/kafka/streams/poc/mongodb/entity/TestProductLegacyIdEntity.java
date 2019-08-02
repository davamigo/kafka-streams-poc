package com.example.kafka.streams.poc.mongodb.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test for product legacy id mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestProductLegacyIdEntity {

    @Test
    public void testEmptyConstructor() {

        ProductLegacyIdEntity entity = new ProductLegacyIdEntity();

        assertNull(entity.getUuid());
        assertEquals(0, entity.getLegacyId());
    }

    @Test
    public void testCopyConstructor() {

        ProductLegacyIdEntity entity = new ProductLegacyIdEntity("101", 102);

        assertEquals("101", entity.getUuid());
        assertEquals(102, entity.getLegacyId());
    }
}
