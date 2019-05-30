package com.example.kafka.streams.poc.domain.entity.warehouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for WarehouseOrderLine.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestWarehouseOrderLineExporter {

    @Test
    public void testExport() {

        WarehouseOrderLine line = WarehouseOrderLine.newBuilder()
                .setUuid("101")
                .setCountry("102")
                .setDate(new Date(103L))
                .setProductUuid("104")
                .setProductLegacyId(105)
                .setProductName("106")
                .setProductBarCode("107")
                .setQuantity(108)
                .build();

        com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine avroLine
                = WarehouseOrderLine.newAvroExporter(line).export();

        assertEquals("101", avroLine.getUuid());
        assertEquals("102", avroLine.getCountry());
        assertEquals(103L, (long) avroLine.getDate());
        assertEquals("104", avroLine.getProductUuid());
        assertEquals(105, (int) avroLine.getProductLegacyId());
        assertEquals("106", avroLine.getProductName());
        assertEquals("107", avroLine.getProductBarCode());
        assertEquals(108, (int) avroLine.getQuantity());
    }
}
