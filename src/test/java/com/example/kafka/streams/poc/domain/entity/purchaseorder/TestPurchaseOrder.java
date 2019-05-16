package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for purchase order domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrder {
    @Test
    public void testDefaultConstructor() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        assertNull(purchaseOrder.getUuid());
        assertNull(purchaseOrder.getAggregationKey());
        assertNull(purchaseOrder.getCountry());
        assertNotNull(purchaseOrder.getDate());
        assertEquals(0f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(0, purchaseOrder.getTotalQuantity());
        assertNotNull(purchaseOrder.getLines());
        assertTrue(purchaseOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructorWithNullValues() {

        PurchaseOrder purchaseOrder = new PurchaseOrder(null, null, null, null, 0f, 0, null);

        assertNull(purchaseOrder.getUuid());
        assertNull(purchaseOrder.getAggregationKey());
        assertNull(purchaseOrder.getCountry());
        assertNotNull(purchaseOrder.getDate());
        assertEquals(0f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(0, purchaseOrder.getTotalQuantity());
        assertNotNull(purchaseOrder.getLines());
        assertTrue(purchaseOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructorWithAllValues() {

        Date date = new Date();

        PurchaseOrderLine line = PurchaseOrderLine.newBuilder().setUuid("111").build();
        List<PurchaseOrderLine> lines = new ArrayList<>();
        lines.add(line);

        PurchaseOrder purchaseOrder = new PurchaseOrder("101", "102", "103", date, 104f, 105, lines);

        assertEquals("101", purchaseOrder.getUuid());
        assertEquals("102", purchaseOrder.getAggregationKey());
        assertEquals("103", purchaseOrder.getCountry());
        assertEquals(date, purchaseOrder.getDate());
        assertEquals(104f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(105, purchaseOrder.getTotalQuantity());
        assertEquals(1, purchaseOrder.getLines().size());
        assertEquals(line, purchaseOrder.getLines().get(0));
    }

    @Test
    public void testTwoPurchaseOrdersAreEqualWhenTheyHaveTheSameKey() {

        PurchaseOrder purchaseOrder1 = new PurchaseOrder("201", null, null, null, 0f, 0, null);
        PurchaseOrder purchaseOrder2 = new PurchaseOrder("201", null, null, null, 0f, 0, null);

        assertEquals(purchaseOrder1, purchaseOrder2);
        assertNotSame(purchaseOrder1, purchaseOrder2);
    }

    @Test
    public void testTwoPurchaseOrdersAreDifferentWhenTheyHaveDifferentKey() {
        PurchaseOrder purchaseOrder1 = new PurchaseOrder("301", null, null, null, 0f, 0, null);
        PurchaseOrder purchaseOrder2 = new PurchaseOrder("401", null, null, null, 0f, 0, null);

        assertNotEquals(purchaseOrder1, purchaseOrder2);
    }

    @Test
    public void testEqualsWithNonPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();

        assertNotEquals(purchaseOrder, new String());
    }
}
