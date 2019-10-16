package com.example.kafka.streams.poc.domain.entity.warehouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for WarehouseOrder.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderBuilder {

    @Test
    public void testBuilderSettersReturnExpectedResults() {

        WarehouseOrder.Builder builder = WarehouseOrder.newBuilder();
        WarehouseOrder warehouseOrder = builder
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(new Date(104))
                .build();

        assertEquals("101", warehouseOrder.getUuid());
        assertEquals("102", warehouseOrder.getAggregationKey());
        assertEquals("103", warehouseOrder.getCountry());
        assertEquals(104, warehouseOrder.getDate().getTime());
        assertTrue(warehouseOrder.getLines().isEmpty());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        WarehouseOrderLineCondensed line = WarehouseOrderLineCondensed.newBuilder().setUuid("211").build();
        List<WarehouseOrderLineCondensed> lines = new ArrayList<>();
        lines.add(line);

        WarehouseOrder source = new WarehouseOrder("201", "202", "203", new Date(204), lines);
        WarehouseOrder.Builder builder = WarehouseOrder.newBuilder().set(source);
        WarehouseOrder warehouseOrder = builder.build();

        assertEquals(source, warehouseOrder);
        assertNotSame(source, warehouseOrder);
        assertEquals("201", warehouseOrder.getUuid());
        assertEquals("202", warehouseOrder.getAggregationKey());
        assertEquals("203", warehouseOrder.getCountry());
        assertEquals(204, warehouseOrder.getDate().getTime());
        assertEquals(1, warehouseOrder.getLines().size());
        assertEquals(line, warehouseOrder.getLines().get(0));
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed sourceLine1
                = com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed.newBuilder()
                        .setUuid("311")
                        .setProductUuid("312")
                        .setProductLegacyId(313)
                        .setProductName("314")
                        .setProductBarCode("315")
                        .setQuantity(316)
                        .build();

        com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed sourceLine2
                = com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed.newBuilder()
                        .setUuid("321")
                        .setProductUuid("322")
                        .setProductLegacyId(323)
                        .setProductName("324")
                        .setProductBarCode("325")
                        .setQuantity(326)
                        .build();

        List<com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed> sourceLines = new ArrayList<>();
        sourceLines.add(sourceLine1);
        sourceLines.add(sourceLine2);

        com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder sourceWarehouseOrder
                = new com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder("301", "302", "303", 304L, sourceLines);

        WarehouseOrder.Builder builder = WarehouseOrder.newBuilder().set(sourceWarehouseOrder);
        WarehouseOrder warehouseOrder = builder.build();

        assertEquals("301", warehouseOrder.getUuid());
        assertEquals("302", warehouseOrder.getAggregationKey());
        assertEquals("303", warehouseOrder.getCountry());
        assertEquals(304, warehouseOrder.getDate().getTime());

        assertEquals(2, warehouseOrder.getLines().size());
        assertEquals("311", warehouseOrder.getLines().get(0).getUuid());
        assertEquals("321", warehouseOrder.getLines().get(1).getUuid());
    }

    @Test
    public void testSetLines() {

        WarehouseOrderLineCondensed warehouseOrderLine1 = WarehouseOrderLineCondensed.newBuilder().setUuid("411").build();
        WarehouseOrderLineCondensed warehouseOrderLine2 = WarehouseOrderLineCondensed.newBuilder().setUuid("421").build();
        List<WarehouseOrderLineCondensed> lines = new ArrayList<>();
        lines.add(warehouseOrderLine1);
        lines.add(warehouseOrderLine2);

        WarehouseOrder.Builder builder = WarehouseOrder.newBuilder();
        WarehouseOrder warehouseOrder = builder
                .setLines(lines)
                .build();

        assertEquals(2, warehouseOrder.getLines().size());
        assertEquals(warehouseOrderLine1, warehouseOrder.getLines().get(0));
        assertEquals(warehouseOrderLine2, warehouseOrder.getLines().get(1));
    }

    @Test
    public void testAddLines() {

        WarehouseOrderLineCondensed warehouseOrderLine1 = WarehouseOrderLineCondensed.newBuilder().setUuid("521").build();
        WarehouseOrderLineCondensed warehouseOrderLine2 = WarehouseOrderLineCondensed.newBuilder().setUuid("521").build();

        WarehouseOrder.Builder builder = WarehouseOrder.newBuilder();
        WarehouseOrder warehouseOrder = builder
                .addLine(warehouseOrderLine1)
                .addLine(warehouseOrderLine2)
                .build();

        assertEquals(2, warehouseOrder.getLines().size());
        assertEquals(warehouseOrderLine1, warehouseOrder.getLines().get(0));
        assertEquals(warehouseOrderLine2, warehouseOrder.getLines().get(1));
    }

    @Test
    public void testClearWarehouseOrderLine() {

        WarehouseOrder warehouseOrder = WarehouseOrder.newBuilder()
                .addLine(new WarehouseOrderLineCondensed())
                .clearLines()
                .build();

        assertTrue(warehouseOrder.getLines().isEmpty());
    }
}
