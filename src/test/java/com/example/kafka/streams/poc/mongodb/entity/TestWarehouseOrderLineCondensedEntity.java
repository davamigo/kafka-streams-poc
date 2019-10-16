package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLineCondensed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test for warehouse order line mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineCondensedEntity {

    @Test
    public void testEmptyConstructor() {

        WarehouseOrderLineCondensedEntity warehouseOrderLineEntity = new WarehouseOrderLineCondensedEntity();

        assertNull(warehouseOrderLineEntity.getUuid());
        assertNull(warehouseOrderLineEntity.getProductUuid());
        assertNull(warehouseOrderLineEntity.getProductLegacyId());
        assertNull(warehouseOrderLineEntity.getProductName());
        assertNull(warehouseOrderLineEntity.getProductBarCode());
        assertEquals(0, warehouseOrderLineEntity.getQuantity());
    }

    @Test
    public void testCopyConstructor() {

        WarehouseOrderLineCondensed warehouseOrderLine = new WarehouseOrderLineCondensed("101", "102", 103, "104", "105", 106);
        WarehouseOrderLineCondensedEntity warehouseOrderLineEntity = new WarehouseOrderLineCondensedEntity(warehouseOrderLine);

        assertEquals("101", warehouseOrderLineEntity.getUuid());
        assertEquals("102", warehouseOrderLineEntity.getProductUuid());
        assertEquals(103, (int) warehouseOrderLineEntity.getProductLegacyId());
        assertEquals("104", warehouseOrderLineEntity.getProductName());
        assertEquals("105", warehouseOrderLineEntity.getProductBarCode());
        assertEquals(106, warehouseOrderLineEntity.getQuantity());
    }
}
