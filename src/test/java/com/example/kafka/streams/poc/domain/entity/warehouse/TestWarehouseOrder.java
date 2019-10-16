package com.example.kafka.streams.poc.domain.entity.warehouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for warehouse order domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrder {
    @Test
    public void testDefaultConstructor() {
        WarehouseOrder warehouseOrder = new WarehouseOrder();

        assertNull(warehouseOrder.getUuid());
        assertNull(warehouseOrder.getAggregationKey());
        assertNull(warehouseOrder.getCountry());
        assertNotNull(warehouseOrder.getDate());
        assertNotNull(warehouseOrder.getLines());
        assertTrue(warehouseOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructorWithNullValues() {

        WarehouseOrder warehouseOrder = new WarehouseOrder(null, null, null, null, null);

        assertNull(warehouseOrder.getUuid());
        assertNull(warehouseOrder.getAggregationKey());
        assertNull(warehouseOrder.getCountry());
        assertNotNull(warehouseOrder.getDate());
        assertNotNull(warehouseOrder.getLines());
        assertTrue(warehouseOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructorWithAllValues() {

        Date date = new Date();

        WarehouseOrderLineCondensed line = WarehouseOrderLineCondensed.newBuilder().setUuid("111").build();
        List<WarehouseOrderLineCondensed> lines = new ArrayList<>();
        lines.add(line);

        WarehouseOrder warehouseOrder = new WarehouseOrder("101", "102", "103", date, lines);

        assertEquals("101", warehouseOrder.getUuid());
        assertEquals("102", warehouseOrder.getAggregationKey());
        assertEquals("103", warehouseOrder.getCountry());
        assertEquals(date, warehouseOrder.getDate());
        assertEquals(1, warehouseOrder.getLines().size());
        assertEquals(line, warehouseOrder.getLines().get(0));
    }

    @Test
    public void testTwoWarehouseOrdersAreEqualWhenTheyHaveTheSameKey() {

        WarehouseOrder warehouseOrder1 = new WarehouseOrder("201", null, null, null, null);
        WarehouseOrder warehouseOrder2 = new WarehouseOrder("201", null, null, null, null);

        assertEquals(warehouseOrder1, warehouseOrder2);
        assertNotSame(warehouseOrder1, warehouseOrder2);
    }

    @Test
    public void testTwoWarehouseOrdersAreDifferentWhenTheyHaveDifferentKey() {
        WarehouseOrder warehouseOrder1 = new WarehouseOrder("301", null, null, null, null);
        WarehouseOrder warehouseOrder2 = new WarehouseOrder("401", null, null, null, null);

        assertNotEquals(warehouseOrder1, warehouseOrder2);
    }

    @Test
    public void testEqualsWithNonWarehouseOrder() {
        WarehouseOrder warehouseOrder = new WarehouseOrder();

        assertNotEquals(warehouseOrder, new String());
    }
}
