package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLineCondensed;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for warehouse order mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderEntity {

    @Test
    public void testEmptyConstructor() {

        WarehouseOrderEntity warehouseOrderEntity = new WarehouseOrderEntity();

        assertNull(warehouseOrderEntity.getUuid());
        assertNull(warehouseOrderEntity.getAggregationKey());
        assertNull(warehouseOrderEntity.getCountry());
        assertNull(warehouseOrderEntity.getDate());
        assertNotNull(warehouseOrderEntity.getLines());
        assertEquals(0, warehouseOrderEntity.getLines().size());
    }

    @Test
    public void testCopyConstructor() {

        List<WarehouseOrderLineCondensed> lines = new ArrayList<>();
        WarehouseOrderLineCondensed line = WarehouseOrderLineCondensed.newBuilder().setUuid("121").build();
        lines.add(line);

        WarehouseOrder warehouseOrder = new WarehouseOrder("101", "102", "103", new Date(104), lines);
        WarehouseOrderEntity warehouseOrderEntity = new WarehouseOrderEntity(warehouseOrder);

        assertEquals("101", warehouseOrderEntity.getUuid());
        assertEquals("102", warehouseOrderEntity.getAggregationKey());
        assertEquals("103", warehouseOrderEntity.getCountry());
        assertEquals(104, warehouseOrderEntity.getDate().getTime());
        assertEquals("121", warehouseOrderEntity.getLines().get(0).getUuid());
    }
}
