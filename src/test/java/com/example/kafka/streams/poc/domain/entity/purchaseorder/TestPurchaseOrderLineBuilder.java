package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit test for PurchaseOrderLine.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderLineBuilder {

    @Test
    public void testBuiderSettersReturnExpectedResults() {

        PurchaseOrderLine.Builder builder = PurchaseOrderLine.newBuilder();
        PurchaseOrderLine line = builder
                .setUuid("101")
                .setAggregationKey("102")
                .setProductUuid("103")
                .setPrice(104f)
                .setQuantity(105)
                .build();

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getAggregationKey());
        assertEquals("103", line.getProductUuid());
        assertEquals(104f, line.getPrice(), 0.001);
        assertEquals(105, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        PurchaseOrderLine source = new PurchaseOrderLine("201", "202", "203", 204f, 205);
        PurchaseOrderLine.Builder builder = PurchaseOrderLine.newBuilder().set(source);
        PurchaseOrderLine line = builder.build();

        assertEquals(source, line);
        assertNotSame(source, line);
        assertEquals("201", line.getUuid());
        assertEquals("202", line.getAggregationKey());
        assertEquals("203", line.getProductUuid());
        assertEquals(204f, line.getPrice(), 0.001);
        assertEquals(205, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine =
                com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed.newBuilder()
                        .setUuid("301")
                        .setAggregationKey("302")
                        .setProductUuid("303")
                        .setPrice(304f)
                        .setQuantity(305)
                        .build();

        PurchaseOrderLine line = PurchaseOrderLine.newBuilder().set(sourceLine).build();

        assertEquals("301", line.getUuid());
        assertEquals("302", line.getAggregationKey());
        assertEquals("303", line.getProductUuid());
        assertEquals(304f, line.getPrice(), 0.001);
        assertEquals(305, line.getQuantity());
    }
}
