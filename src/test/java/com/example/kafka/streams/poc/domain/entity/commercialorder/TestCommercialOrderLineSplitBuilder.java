package com.example.kafka.streams.poc.domain.entity.commercialorder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for CommercialOrderLineSplit.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderLineSplitBuilder {
    @Test
    public void testBuilderSettersReturnExpectedResults() {

        CommercialOrderLineSplit line = CommercialOrderLineSplit
                .newBuilder()
                .setUuid("101")
                .setCommercialOrderUuid("102")
                .setCommercialOrderDatetime(new Date(103L))
                .setShippingCountry("104")
                .setMemberUuid("105")
                .setProductUuid("106")
                .setProductName("107")
                .setProductType("108")
                .setProductBarCode("109")
                .setProductPrice(110f)
                .setOrderLinePrice(111f)
                .setQuantity(112)
                .build();

        Assert.assertEquals("101", line.getUuid());
        Assert.assertEquals("102", line.getCommercialOrderUuid());
        Assert.assertEquals(103L, line.getCommercialOrderDatetime().getTime());
        Assert.assertEquals("104", line.getShippingCountry());
        Assert.assertEquals("105", line.getMemberUuid());
        Assert.assertEquals("106", line.getProductUuid());
        Assert.assertEquals("107", line.getProductName());
        Assert.assertEquals("108", line.getProductType());
        Assert.assertEquals("109", line.getProductBarCode());
        Assert.assertEquals(110f, line.getProductPrice(), 0.001);
        Assert.assertEquals(111f, line.getOrderLinePrice(), 0.001);
        Assert.assertEquals(112, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        CommercialOrderLineSplit source = new CommercialOrderLineSplit(
                "201",
                "202",
                new Date(203L),
                "204",
                "205",
                "206",
                "207",
                "208",
                "209",
                210f,
                211f,
                212
        );

        CommercialOrderLineSplit line = CommercialOrderLineSplit
                .newBuilder()
                .set(source)
                .build();

        Assert.assertEquals("201", line.getUuid());
        Assert.assertEquals("202", line.getCommercialOrderUuid());
        Assert.assertEquals(203L, line.getCommercialOrderDatetime().getTime());
        Assert.assertEquals("204", line.getShippingCountry());
        Assert.assertEquals("205", line.getMemberUuid());
        Assert.assertEquals("206", line.getProductUuid());
        Assert.assertEquals("207", line.getProductName());
        Assert.assertEquals("208", line.getProductType());
        Assert.assertEquals("209", line.getProductBarCode());
        Assert.assertEquals(210f, line.getProductPrice(), 0.001);
        Assert.assertEquals(211f, line.getOrderLinePrice(), 0.001);
        Assert.assertEquals(212, line.getQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit sourceLine =
                com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit
                        .newBuilder()
                        .setUuid("301")
                        .setCommercialOrderUuid("302")
                        .setCommercialOrderDatetime(303L)
                        .setShippingCountry("304")
                        .setMemberUuid("305")
                        .setProductUuid("306")
                        .setProductName("307")
                        .setProductType("308")
                        .setProductBarCode("309")
                        .setProductPrice(310f)
                        .setOrderLinePrice(311f)
                        .setQuantity(312)
                        .build();

        CommercialOrderLineSplit line = CommercialOrderLineSplit
                .newBuilder()
                .set(sourceLine)
                .build();

        Assert.assertEquals("301", line.getUuid());
        Assert.assertEquals("302", line.getCommercialOrderUuid());
        Assert.assertEquals(303L, line.getCommercialOrderDatetime().getTime());
        Assert.assertEquals("304", line.getShippingCountry());
        Assert.assertEquals("305", line.getMemberUuid());
        Assert.assertEquals("306", line.getProductUuid());
        Assert.assertEquals("307", line.getProductName());
        Assert.assertEquals("308", line.getProductType());
        Assert.assertEquals("309", line.getProductBarCode());
        Assert.assertEquals(310f, line.getProductPrice(), 0.001);
        Assert.assertEquals(311f, line.getOrderLinePrice(), 0.001);
        Assert.assertEquals(312, line.getQuantity());
    }
}
