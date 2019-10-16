package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for PurchaseOrderLine.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderLineBuilder {

    @Test
    public void testBuilderSettersReturnExpectedResults() {

        PurchaseOrderLine.Builder builder = PurchaseOrderLine.newBuilder();
        PurchaseOrderLine line = builder
                .setUuid("101")
                .setAggregationKey("102")
                .setCountry("103")
                .setDate(new Date(104L))
                .setProductUuid("105")
                .setProductName("106")
                .setProductType("107")
                .setProductBarCode("108")
                .setProductPrice(109f)
                .setQuantity(110)
                .build();

        Assert.assertEquals("101", line.getUuid());
        Assert.assertEquals("102", line.getAggregationKey());
        Assert.assertEquals("103", line.getCountry());
        Assert.assertEquals(104L, line.getDate().getTime());
        Assert.assertEquals("105", line.getProductUuid());
        Assert.assertEquals("106", line.getProductName());
        Assert.assertEquals("107", line.getProductType());
        Assert.assertEquals("108", line.getProductBarCode());
        Assert.assertEquals(109f, line.getProductPrice(), 0.001);
        Assert.assertEquals(110, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        PurchaseOrderLine source = new PurchaseOrderLine(
                "201",
                "202",
                "203",
                new Date(204L),
                "205",
                "206",
                "207",
                "208",
                209f,
                210
        );

        PurchaseOrderLine line = PurchaseOrderLine
                .newBuilder()
                .set(source)
                .build();

        Assert.assertEquals("201", line.getUuid());
        Assert.assertEquals("202", line.getAggregationKey());
        Assert.assertEquals("203", line.getCountry());
        Assert.assertEquals(204L, line.getDate().getTime());
        Assert.assertEquals("205", line.getProductUuid());
        Assert.assertEquals("206", line.getProductName());
        Assert.assertEquals("207", line.getProductType());
        Assert.assertEquals("208", line.getProductBarCode());
        Assert.assertEquals(209f, line.getProductPrice(), 0.001);
        Assert.assertEquals(210, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine sourceLine =
                com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine.newBuilder()
                        .setUuid("301")
                        .setAggregationKey("302")
                        .setCountry("303")
                        .setDate(304L)
                        .setProductUuid("305")
                        .setProductName("306")
                        .setProductType("307")
                        .setProductBarCode("308")
                        .setProductPrice(309f)
                        .setQuantity(310)
                        .build();

        PurchaseOrderLine line = PurchaseOrderLine
                .newBuilder()
                .set(sourceLine)
                .build();

        Assert.assertEquals("301", line.getUuid());
        Assert.assertEquals("302", line.getAggregationKey());
        Assert.assertEquals("303", line.getCountry());
        Assert.assertEquals(304L, line.getDate().getTime());
        Assert.assertEquals("305", line.getProductUuid());
        Assert.assertEquals("306", line.getProductName());
        Assert.assertEquals("307", line.getProductType());
        Assert.assertEquals("308", line.getProductBarCode());
        Assert.assertEquals(309f, line.getProductPrice(), 0.001);
        Assert.assertEquals(310, line.getQuantity());
    }
}
