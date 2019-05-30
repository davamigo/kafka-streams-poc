package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for warehouse order line mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineEntity {

    @Test
    public void testEmptyConstructor() {

        WarehouseOrderLineEntity entity = new WarehouseOrderLineEntity();

        Assert.assertNull(entity.getUuid());
        Assert.assertNull(entity.getCountry());
        Assert.assertNull(entity.getDate());
        Assert.assertNull(entity.getProductUuid());
        Assert.assertNull(entity.getProductLegacyId());
        Assert.assertNull(entity.getProductName());
        Assert.assertNull(entity.getProductBarCode());
        Assert.assertEquals(0, entity.getQuantity());
    }

    @Test
    public void testCopyConstructor() {

        WarehouseOrderLine line = new WarehouseOrderLine("101", "102", new Date(103L), "104", 105, "106", "107", 108);
        WarehouseOrderLineEntity entity = new WarehouseOrderLineEntity(line);

        Assert.assertEquals("101", entity.getUuid());
        Assert.assertEquals("102", entity.getCountry());
        Assert.assertEquals(103L, entity.getDate().getTime());
        Assert.assertEquals("104", entity.getProductUuid());
        Assert.assertEquals(105, (int) entity.getProductLegacyId());
        Assert.assertEquals("106", entity.getProductName());
        Assert.assertEquals("107", entity.getProductBarCode());
        Assert.assertEquals(108, entity.getQuantity());
    }
}
