package com.example.kafka.streams.poc.domain.entity.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for CommercialOrderLine.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderLineBuilder {
    @Test
    public void testBuiderSettersReturnExpectedResults() {

        CommercialOrderLine.Builder builder = CommercialOrderLine.newBuilder();
        CommercialOrderLine line = builder
                .setUuid("101")
                .setCommercialOrderUuid("102")
                .setProductUuid("103")
                .setPrice(104f)
                .setQuantity(105)
                .build();

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getCommercialOrderUuid());
        assertEquals("103", line.getProductUuid());
        assertEquals(104f, line.getPrice(), 0.001);
        assertEquals(105, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        CommercialOrderLine source = new CommercialOrderLine("201", "202", "203", 204f, 205);
        CommercialOrderLine.Builder builder = CommercialOrderLine.newBuilder().set(source);
        CommercialOrderLine commercialOrderLine = builder.build();

        assertEquals(source, commercialOrderLine);
        assertNotSame(source, commercialOrderLine);
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.order.CommercialOrderLine sourceLine =
                com.example.kafka.streams.poc.schemas.order.CommercialOrderLine.newBuilder()
                        .setUuid("301")
                        .setCommercialOrderUuid("302")
                        .setProductUuid("303")
                        .setPrice(304f)
                        .setQuantity(305)
                        .build();

        CommercialOrderLine line = CommercialOrderLine.newBuilder().set(sourceLine).build();

        assertEquals("301", line.getUuid());
        assertEquals("302", line.getCommercialOrderUuid());
        assertEquals("303", line.getProductUuid());
        assertEquals(304f, line.getPrice(), 0.001);
        assertEquals(305, line.getQuantity());
    }
}
