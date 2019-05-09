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
 * Unit test for PurchaseOrder.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderBuilder {

    @Test
    public void testBuilderSettersReturnExpectedResults() {

        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder();
        PurchaseOrder purchaseOrder = builder
                .setKey("101")
                .setCountry("102")
                .setDate(new Date(103))
                .setTotalAmount(104f)
                .setTotalQuantity(105)
                .build();

        assertEquals("101", purchaseOrder.getKey());
        assertEquals("102", purchaseOrder.getCountry());
        assertEquals(103, purchaseOrder.getDate().getTime());
        assertEquals(104f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(105, purchaseOrder.getTotalQuantity());
        assertTrue(purchaseOrder.getLines().isEmpty());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        PurchaseOrderLine line = PurchaseOrderLine.newBuilder().setKey("211").build();
        List<PurchaseOrderLine> lines = new ArrayList<>();
        lines.add(line);

        PurchaseOrder source = new PurchaseOrder("201", "202", new Date(203), 204f, 205, lines);
        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder().set(source);
        PurchaseOrder purchaseOrder = builder.build();

        assertEquals(source, purchaseOrder);
        assertNotSame(source, purchaseOrder);
        assertEquals("201", purchaseOrder.getKey());
        assertEquals("202", purchaseOrder.getCountry());
        assertEquals(203, purchaseOrder.getDate().getTime());
        assertEquals(204f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(205, purchaseOrder.getTotalQuantity());
        assertEquals(1, purchaseOrder.getLines().size());
        assertEquals(line, purchaseOrder.getLines().get(0));
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine1
                = com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed.newBuilder()
                        .setPurchaseOrderLineKey("311")
                        .setProductUuid("312")
                        .setPrice(313f)
                        .setQuantity(314)
                        .build();

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine2
                = com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed.newBuilder()
                        .setPurchaseOrderLineKey("321")
                        .setProductUuid("322")
                        .setPrice(343f)
                        .setQuantity(344)
                        .build();

        List<com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed> sourceLines = new ArrayList<>();
        sourceLines.add(sourceLine1);
        sourceLines.add(sourceLine2);

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder sourcePurchaseOrder
                = new com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder("301", "302", 303L, sourceLines, 304f, 305);

        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder().set(sourcePurchaseOrder);
        PurchaseOrder purchaseOrder = builder.build();

        assertEquals("301", purchaseOrder.getKey());
        assertEquals("302", purchaseOrder.getCountry());
        assertEquals(303, purchaseOrder.getDate().getTime());
        assertEquals(304f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(305, purchaseOrder.getTotalQuantity());

        assertEquals(2, purchaseOrder.getLines().size());
        assertEquals("311", purchaseOrder.getLines().get(0).getKey());
        assertEquals("321", purchaseOrder.getLines().get(1).getKey());
    }

    @Test
    public void testSetLines() {

        PurchaseOrderLine purchaseOrderLine1 = PurchaseOrderLine.newBuilder().setKey("411").build();
        PurchaseOrderLine purchaseOrderLine2 = PurchaseOrderLine.newBuilder().setKey("421").build();
        List<PurchaseOrderLine> lines = new ArrayList<>();
        lines.add(purchaseOrderLine1);
        lines.add(purchaseOrderLine2);

        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder();
        PurchaseOrder purchaseOrder = builder
                .setLines(lines)
                .build();

        assertEquals(2, purchaseOrder.getLines().size());
        assertEquals(purchaseOrderLine1, purchaseOrder.getLines().get(0));
        assertEquals(purchaseOrderLine2, purchaseOrder.getLines().get(1));
    }

    @Test
    public void testAddLines() {

        PurchaseOrderLine purchaseOrderLine1 = PurchaseOrderLine.newBuilder().setKey("521").build();
        PurchaseOrderLine purchaseOrderLine2 = PurchaseOrderLine.newBuilder().setKey("521").build();

        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder();
        PurchaseOrder purchaseOrder = builder
                .addLine(purchaseOrderLine1)
                .addLine(purchaseOrderLine2)
                .build();

        assertEquals(2, purchaseOrder.getLines().size());
        assertEquals(purchaseOrderLine1, purchaseOrder.getLines().get(0));
        assertEquals(purchaseOrderLine2, purchaseOrder.getLines().get(1));
    }

    @Test
    public void testClearPurchaseOrderLine() {

        PurchaseOrder purchaseOrder = PurchaseOrder.newBuilder()
                .addLine(new PurchaseOrderLine())
                .clearLines()
                .build();

        assertTrue(purchaseOrder.getLines().isEmpty());
    }
}
