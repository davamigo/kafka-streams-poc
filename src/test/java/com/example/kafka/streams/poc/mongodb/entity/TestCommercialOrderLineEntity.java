package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.order.CommercialOrderLine;
import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test for commercial order line mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderLineEntity {

    @Test
    public void testEmptyConstructor() {

        CommercialOrderLineEntity commercialOrderLineEntity = new CommercialOrderLineEntity();

        assertNull(commercialOrderLineEntity.getUuid());
        assertNull(commercialOrderLineEntity.getProductUuid());
        assertEquals(0, commercialOrderLineEntity.getPrice(), 0.001);
        assertEquals(0, commercialOrderLineEntity.getQuantity());
    }

    @Test
    public void testCopyConstructor() {

        Product product = new Product("111", "112", "113", "114", 115f);
        CommercialOrderLine commercialOrderLine = new CommercialOrderLine("101", "102", product, 104f, 105);
        CommercialOrderLineEntity commercialOrderLineEntity = new CommercialOrderLineEntity(commercialOrderLine);

        assertEquals("101", commercialOrderLineEntity.getUuid());
        assertEquals("111", commercialOrderLineEntity.getProductUuid());
        assertEquals(104f, commercialOrderLineEntity.getPrice(), 0.001);
        assertEquals(105, commercialOrderLineEntity.getQuantity());
    }
}
