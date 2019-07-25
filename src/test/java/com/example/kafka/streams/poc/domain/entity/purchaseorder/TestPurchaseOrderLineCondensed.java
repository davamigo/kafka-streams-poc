package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for PurchaseOrderLineCondensed domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderLineCondensed {

    @Test
    public void testDefaultConstructor() {
        PurchaseOrderLineCondensed line = new PurchaseOrderLineCondensed();

        assertNull(line.getUuid());
        assertNull(line.getAggregationKey());
        assertNull(line.getProductUuid());
        assertEquals(0f, line.getPrice(), 0.001);
        assertEquals(1, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {
        PurchaseOrderLineCondensed line = new PurchaseOrderLineCondensed("101", "102", "103", 104f, 105);

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getAggregationKey());
        assertEquals("103", line.getProductUuid());
        assertEquals(104f, line.getPrice(), 0.001);
        assertEquals(105, line.getQuantity());
    }

    @Test
    public void testTwoLinesAreEqualWhenTheyHaveTheSameKey() {
        PurchaseOrderLineCondensed line1 = new PurchaseOrderLineCondensed("201", "202", "203", 204f, 205);
        PurchaseOrderLineCondensed line2 = new PurchaseOrderLineCondensed("201", "212", "203", 214f, 215);

        assertEquals(line1, line2);
        assertNotSame(line1, line2);
    }

    @Test
    public void testTwoPurchaseOrderLinesAreDifferentWhenTheyHaveDifferentKey() {
        PurchaseOrderLineCondensed line1 = new PurchaseOrderLineCondensed("301", "302", "303", 304f, 305);
        PurchaseOrderLineCondensed line2 = new PurchaseOrderLineCondensed("401", "302", "303", 304f, 305);

        assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonPurchaseOrderLine() {
        PurchaseOrderLineCondensed purchaseOrderLine = new PurchaseOrderLineCondensed();

        assertNotEquals(purchaseOrderLine, new String());
    }
}
