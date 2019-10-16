package com.example.kafka.streams.poc.domain.entity.warehouse;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineCondensedEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit test for WarehouseOrderLineCondensed.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineCondensedBuilder {

    @Test
    public void testBuilderSettersReturnExpectedResults() {

        WarehouseOrderLineCondensed.Builder builder = WarehouseOrderLineCondensed.newBuilder();
        WarehouseOrderLineCondensed line = builder
                .setUuid("101")
                .setProductUuid("102")
                .setProductLegacyId(103)
                .setProductName("104")
                .setProductBarCode("105")
                .setQuantity(106)
                .build();

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getProductUuid());
        assertEquals(103, (int) line.getProductLegacyId());
        assertEquals("104", line.getProductName());
        assertEquals("105", line.getProductBarCode());
        assertEquals(106, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        WarehouseOrderLineCondensed source = new WarehouseOrderLineCondensed("201", "202", 203, "204", "205", 206);
        WarehouseOrderLineCondensed.Builder builder = WarehouseOrderLineCondensed.newBuilder().set(source);
        WarehouseOrderLineCondensed line = builder.build();

        assertEquals(source, line);
        assertNotSame(source, line);

        assertEquals("201", line.getUuid());
        assertEquals("202", line.getProductUuid());
        assertEquals(203, (int) line.getProductLegacyId());
        assertEquals("204", line.getProductName());
        assertEquals("205", line.getProductBarCode());
        assertEquals(206, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed sourceLine =
                com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed.newBuilder()
                        .setUuid("301")
                        .setProductUuid("302")
                        .setProductLegacyId(303)
                        .setProductName("304")
                        .setProductBarCode("305")
                        .setQuantity(306)
                        .build();

        WarehouseOrderLineCondensed line = WarehouseOrderLineCondensed.newBuilder().set(sourceLine).build();

        assertEquals("301", line.getUuid());
        assertEquals("302", line.getProductUuid());
        assertEquals(303, (int) line.getProductLegacyId());
        assertEquals("304", line.getProductName());
        assertEquals("305", line.getProductBarCode());
        assertEquals(306, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromMongoEntity() {

        WarehouseOrderLineCondensed sourceLine = new WarehouseOrderLineCondensed("401", "402", 403, "404", "405", 406);
        WarehouseOrderLineCondensedEntity sourceEntity = new WarehouseOrderLineCondensedEntity(sourceLine);
        WarehouseOrderLineCondensed line = WarehouseOrderLineCondensed.newBuilder().set(sourceEntity).build();

        assertEquals("401", line.getUuid());
        assertEquals("402", line.getProductUuid());
        assertEquals(403, (int) line.getProductLegacyId());
        assertEquals("404", line.getProductName());
        assertEquals("405", line.getProductBarCode());
        assertEquals(406, line.getQuantity());
    }
}
