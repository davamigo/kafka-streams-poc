package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for PurchaseOrderLine domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderLine {

    @Test
    public void testDefaultConstructor() {
        PurchaseOrderLine line = new PurchaseOrderLine();

        assertNull(line.getKey());
        assertNull(line.getProductUuid());
        assertEquals(0f, line.getPrice(), 0.001);
        assertEquals(1, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {
        PurchaseOrderLine line = new PurchaseOrderLine("101", "102", 103f, 104);

        assertEquals("101", line.getKey());
        assertEquals("102", line.getProductUuid());
        assertEquals(103f, line.getPrice(), 0.001);
        assertEquals(104, line.getQuantity());
    }

    @Test
    public void testTwoLinesAreEqualWhenTheyHaveTheSameKey() {
        PurchaseOrderLine line1 = new PurchaseOrderLine("201", "202", 203f, 204);
        PurchaseOrderLine line2 = new PurchaseOrderLine("201", "212", 213f, 214);

        assertEquals(line1, line2);
        assertNotSame(line1, line2);
    }

    @Test
    public void testTwoPurchaseOrderLinesAreDifferentWhenTheyHaveDifferentKey() {
        PurchaseOrderLine line1 = new PurchaseOrderLine("301", "302", 303f, 304);
        PurchaseOrderLine line2 = new PurchaseOrderLine("401", "302", 303f, 304);

        assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonPurchaseOrderLine() {
        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();

        assertNotEquals(purchaseOrderLine, new String());
    }
}
