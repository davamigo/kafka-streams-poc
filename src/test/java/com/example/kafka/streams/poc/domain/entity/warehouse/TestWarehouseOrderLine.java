package com.example.kafka.streams.poc.domain.entity.warehouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit test for WarehouseOrderLine domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLine {

    @Test
    public void testDefaultConstructor() {
        WarehouseOrderLine line = new WarehouseOrderLine();

        assertNull(line.getUuid());
        assertNull(line.getCountry());
        assertNull(line.getDate());
        assertNull(line.getProductUuid());
        assertNull(line.getProductLegacyId());
        assertNull(line.getProductName());
        assertNull(line.getProductBarCode());
        assertEquals(0, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {
        WarehouseOrderLine line = new WarehouseOrderLine("101", "102", new Date(103L), "104", 105, "106", "107", 108);

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getCountry());
        assertEquals(103L, line.getDate().getTime());
        assertEquals("104", line.getProductUuid());
        assertEquals(105, (int) line.getProductLegacyId());
        assertEquals("106", line.getProductName());
        assertEquals("107", line.getProductBarCode());
        assertEquals(108, line.getQuantity());
    }

    @Test
    public void testTwoLinesAreEqualWhenTheyHaveTheSameKey() {
        WarehouseOrderLine line1 = new WarehouseOrderLine("201", "202", new Date(203L), "204", 205, "206", "207", 208);
        WarehouseOrderLine line2 = new WarehouseOrderLine("201", "212", new Date(213L), "214", 215, "216", "217", 218);

        assertEquals(line1, line2);
        assertNotSame(line1, line2);
    }

    @Test
    public void testTwoWarehouseOrderLinesAreDifferentWhenTheyHaveDifferentKey() {
        WarehouseOrderLine line1 = new WarehouseOrderLine("301", "302", new Date(303L), "304", 305, "306", "307", 308);
        WarehouseOrderLine line2 = new WarehouseOrderLine("311", "302", new Date(303L), "304", 305, "306", "307", 308);

        assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonWarehouseOrderLine() {
        WarehouseOrderLine warehouseOrderLine = new WarehouseOrderLine();

        assertNotEquals(warehouseOrderLine, new String());
    }
}
