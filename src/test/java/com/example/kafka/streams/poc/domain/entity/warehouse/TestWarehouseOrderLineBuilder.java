package com.example.kafka.streams.poc.domain.entity.warehouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit test for WarehouseOrderLine.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineBuilder {

    @Test
    public void testBuiderSettersReturnExpectedResults() {

        WarehouseOrderLine.Builder builder = WarehouseOrderLine.newBuilder();
        WarehouseOrderLine line = builder
                .setUuid("101")
                .setCountry("102")
                .setDate(new Date(103L))
                .setProductUuid("104")
                .setProductLegacyId(105)
                .setProductName("106")
                .setProductBarCode("107")
                .setQuantity(108)
                .build();

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
    public void testSetCopiesTheContentFromSourceObject() {

        WarehouseOrderLine source = new WarehouseOrderLine("201", "202", new Date(203L), "204", 205, "206", "207", 208);
        WarehouseOrderLine.Builder builder = WarehouseOrderLine.newBuilder().set(source);
        WarehouseOrderLine line = builder.build();

        assertEquals(source, line);
        assertNotSame(source, line);

        assertEquals("201", line.getUuid());
        assertEquals("202", line.getCountry());
        assertEquals(203L, line.getDate().getTime());
        assertEquals("204", line.getProductUuid());
        assertEquals(205, (int) line.getProductLegacyId());
        assertEquals("206", line.getProductName());
        assertEquals("207", line.getProductBarCode());
        assertEquals(208, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine sourceLine =
                com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine.newBuilder()
                        .setUuid("301")
                        .setCountry("302")
                        .setDate(303L)
                        .setProductUuid("304")
                        .setProductLegacyId(305)
                        .setProductName("306")
                        .setProductBarCode("307")
                        .setQuantity(308)
                        .build();

        WarehouseOrderLine line = WarehouseOrderLine.newBuilder().set(sourceLine).build();

        assertEquals("301", line.getUuid());
        assertEquals("302", line.getCountry());
        assertEquals(303L, line.getDate().getTime());
        assertEquals("304", line.getProductUuid());
        assertEquals(305, (int) line.getProductLegacyId());
        assertEquals("306", line.getProductName());
        assertEquals("307", line.getProductBarCode());
        assertEquals(308, line.getQuantity());
    }
}
