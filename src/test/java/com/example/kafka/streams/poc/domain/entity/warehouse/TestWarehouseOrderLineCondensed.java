package com.example.kafka.streams.poc.domain.entity.warehouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for WarehouseOrderLineCondensed domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineCondensed {

    @Test
    public void testDefaultConstructor() {
        WarehouseOrderLineCondensed line = new WarehouseOrderLineCondensed();

        assertNull(line.getUuid());
        assertNull(line.getProductUuid());
        assertNull(line.getProductLegacyId());
        assertNull(line.getProductName());
        assertNull(line.getProductBarCode());
        assertEquals(0, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {
        WarehouseOrderLineCondensed line = new WarehouseOrderLineCondensed(
                "101",
                "102",
                103,
                "104",
                "105",
                106
        );

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getProductUuid());
        assertEquals(103, (int) line.getProductLegacyId());
        assertEquals("104", line.getProductName());
        assertEquals("105", line.getProductBarCode());
        assertEquals(106, line.getQuantity());
    }

    @Test
    public void testTwoLinesAreEqualWhenTheyHaveTheSameKey() {
        WarehouseOrderLineCondensed line1 = new WarehouseOrderLineCondensed(
                "201",
                "202",
                203,
                "204",
                "205",
                206
        );

        WarehouseOrderLineCondensed line2 = new WarehouseOrderLineCondensed(
                "201",
                "212",
                213,
                "214",
                "215",
                216
        );

        assertEquals(line1, line2);
        assertNotSame(line1, line2);
    }

    @Test
    public void testTwoWarehouseOrderLinesAreDifferentWhenTheyHaveDifferentKey() {

        WarehouseOrderLineCondensed line1 = new WarehouseOrderLineCondensed(
                "301",
                "302",
                303,
                "304",
                "305",
                306
        );

        WarehouseOrderLineCondensed line2 = new WarehouseOrderLineCondensed(
                "311",
                "302",
                303,
                "304",
                "305",
                306
        );

        assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonWarehouseOrderLine() {
        WarehouseOrderLineCondensed warehouseOrderLine = new WarehouseOrderLineCondensed();

        assertNotEquals(warehouseOrderLine, new String());
    }
}
