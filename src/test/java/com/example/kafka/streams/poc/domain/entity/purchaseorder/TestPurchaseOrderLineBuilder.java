package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

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
                .setKey("101")
                .setProductUuid("102")
                .setPrice(103f)
                .setQuantity(104)
                .build();

        assertEquals("101", line.getKey());
        assertEquals("102", line.getProductUuid());
        assertEquals(103f, line.getPrice(), 0.001);
        assertEquals(104, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        PurchaseOrderLine source = new PurchaseOrderLine("201", "202", 203f, 204);
        PurchaseOrderLine.Builder builder = PurchaseOrderLine.newBuilder().set(source);
        PurchaseOrderLine line = builder.build();

        assertEquals(source, line);
        assertNotSame(source, line);
        assertEquals("201", line.getKey());
        assertEquals("202", line.getProductUuid());
        assertEquals(203f, line.getPrice(), 0.001);
        assertEquals(204, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine =
                com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed.newBuilder()
                        .setPurchaseOrderLineKey("301")
                        .setProductUuid("302")
                        .setPrice(303f)
                        .setQuantity(304)
                        .build();

        PurchaseOrderLine line = PurchaseOrderLine.newBuilder().set(sourceLine).build();

        assertEquals("301", line.getKey());
        assertEquals("302", line.getProductUuid());
        assertEquals(303f, line.getPrice(), 0.001);
        assertEquals(304, line.getQuantity());
    }
}
