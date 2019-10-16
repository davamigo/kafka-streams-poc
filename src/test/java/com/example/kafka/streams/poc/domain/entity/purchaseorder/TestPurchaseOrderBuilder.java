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
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(new Date(104))
                .setTotalAmount(105f)
                .setTotalQuantity(106)
                .build();

        assertEquals("101", purchaseOrder.getUuid());
        assertEquals("102", purchaseOrder.getAggregationKey());
        assertEquals("103", purchaseOrder.getCountry());
        assertEquals(104, purchaseOrder.getDate().getTime());
        assertEquals(105f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(106, purchaseOrder.getTotalQuantity());
        assertTrue(purchaseOrder.getLines().isEmpty());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        PurchaseOrderLineCondensed line = PurchaseOrderLineCondensed.newBuilder().setUuid("211").build();
        List<PurchaseOrderLineCondensed> lines = new ArrayList<>();
        lines.add(line);

        PurchaseOrder source = new PurchaseOrder("201", "202", "203", new Date(204), 205f, 206, lines);
        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder().set(source);
        PurchaseOrder purchaseOrder = builder.build();

        assertEquals(source, purchaseOrder);
        assertNotSame(source, purchaseOrder);
        assertEquals("201", purchaseOrder.getUuid());
        assertEquals("202", purchaseOrder.getAggregationKey());
        assertEquals("203", purchaseOrder.getCountry());
        assertEquals(204, purchaseOrder.getDate().getTime());
        assertEquals(205f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(206, purchaseOrder.getTotalQuantity());
        assertEquals(1, purchaseOrder.getLines().size());
        assertEquals(line, purchaseOrder.getLines().get(0));
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine1
                = com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed.newBuilder()
                        .setUuid("311")
                        .setAggregationKey("312")
                        .setProductUuid("313")
                        .setPrice(314f)
                        .setQuantity(315)
                        .build();

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine2
                = com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed.newBuilder()
                        .setUuid("321")
                        .setAggregationKey("322")
                        .setProductUuid("323")
                        .setPrice(324f)
                        .setQuantity(325)
                        .build();

        List<com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed> sourceLines = new ArrayList<>();
        sourceLines.add(sourceLine1);
        sourceLines.add(sourceLine2);

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder sourcePurchaseOrder
                = new com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder("301", "302", "303", 304L, sourceLines, 305f, 306);

        PurchaseOrder.Builder builder = PurchaseOrder.newBuilder().set(sourcePurchaseOrder);
        PurchaseOrder purchaseOrder = builder.build();

        assertEquals("301", purchaseOrder.getUuid());
        assertEquals("302", purchaseOrder.getAggregationKey());
        assertEquals("303", purchaseOrder.getCountry());
        assertEquals(304, purchaseOrder.getDate().getTime());
        assertEquals(305f, purchaseOrder.getTotalAmount(), 0.001);
        assertEquals(306, purchaseOrder.getTotalQuantity());

        assertEquals(2, purchaseOrder.getLines().size());
        assertEquals("311", purchaseOrder.getLines().get(0).getUuid());
        assertEquals("321", purchaseOrder.getLines().get(1).getUuid());
    }

    @Test
    public void testSetLines() {

        PurchaseOrderLineCondensed purchaseOrderLine1 = PurchaseOrderLineCondensed.newBuilder().setUuid("411").build();
        PurchaseOrderLineCondensed purchaseOrderLine2 = PurchaseOrderLineCondensed.newBuilder().setUuid("421").build();
        List<PurchaseOrderLineCondensed> lines = new ArrayList<>();
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

        PurchaseOrderLineCondensed purchaseOrderLine1 = PurchaseOrderLineCondensed.newBuilder().setUuid("521").build();
        PurchaseOrderLineCondensed purchaseOrderLine2 = PurchaseOrderLineCondensed.newBuilder().setUuid("521").build();

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
                .addLine(new PurchaseOrderLineCondensed())
                .clearLines()
                .build();

        assertTrue(purchaseOrder.getLines().isEmpty());
    }
}
