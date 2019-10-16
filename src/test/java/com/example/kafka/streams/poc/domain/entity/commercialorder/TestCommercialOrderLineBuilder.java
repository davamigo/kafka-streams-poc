package com.example.kafka.streams.poc.domain.entity.commercialorder;

import com.example.kafka.streams.poc.domain.entity.product.Product;
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
    public void testBuilderSettersReturnExpectedResults() {

        Product product = new Product("111", "112", "113", "114", 115f);
        CommercialOrderLine.Builder builder = CommercialOrderLine.newBuilder();
        CommercialOrderLine line = builder
                .setUuid("101")
                .setCommercialOrderUuid("102")
                .setProduct(product)
                .setPrice(104f)
                .setQuantity(105)
                .build();

        assertEquals("101", line.getUuid());
        assertEquals("102", line.getCommercialOrderUuid());
        assertEquals(product, line.getProduct());
        assertEquals(104f, line.getPrice(), 0.001);
        assertEquals(105, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        Product product = new Product("211", "212", "213", "214", 215f);
        CommercialOrderLine source = new CommercialOrderLine("201", "202", product, 204f, 205);
        CommercialOrderLine.Builder builder = CommercialOrderLine.newBuilder().set(source);
        CommercialOrderLine line = builder.build();

        assertEquals(source, line);
        assertNotSame(source, line);
        assertEquals("201", line.getUuid());
        assertEquals("202", line.getCommercialOrderUuid());
        assertEquals(product, line.getProduct());
        assertEquals(204f, line.getPrice(), 0.001);
        assertEquals(205, line.getQuantity());
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
        assertEquals("303", line.getProduct().getUuid());
        assertEquals(304f, line.getPrice(), 0.001);
        assertEquals(305, line.getQuantity());
    }
}
