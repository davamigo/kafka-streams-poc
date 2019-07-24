package com.example.kafka.streams.poc.domain.entity.commercialorder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for CommercialOrderLineSplit domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderLineSplit {

    @Test
    public void testDefaultConstructor() {
        CommercialOrderLineSplit line = new CommercialOrderLineSplit();

        Assert.assertNull(line.getUuid());
        Assert.assertNull(line.getCommercialOrderUuid());
        Assert.assertNotNull(line.getCommercialOrderDatetime());
        Assert.assertNull(line.getShippingCountry());
        Assert.assertNull(line.getMemberUuid());
        Assert.assertNull(line.getProductUuid());
        Assert.assertNull(line.getProductName());
        Assert.assertNull(line.getProductType());
        Assert.assertNull(line.getProductBarCode());
        Assert.assertEquals(0f, line.getProductPrice(), 0.001);
        Assert.assertEquals(0f, line.getOrderLinePrice(), 0.001);
        Assert.assertEquals(0, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {

        CommercialOrderLineSplit line = new CommercialOrderLineSplit(
                "101",
                "102",
                new Date(103L),
                "104",
                "105",
                "106",
                "107",
                "108",
                "109",
                110f,
                111f,
                112
        );

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
    public void testTwoLinesAreEqualWhenTheyHaveTheSameUuid() {

        CommercialOrderLineSplit line1 = new CommercialOrderLineSplit("201", "202", new Date(203L), "204", "205", "206", "207", "208", "209", 210f, 211f, 212);
        CommercialOrderLineSplit line2 = new CommercialOrderLineSplit("201", null, null, null, null, null, null, null, null, null, null, null);

        Assert.assertEquals(line1, line2);
        Assert.assertNotSame(line1, line2);
    }

    @Test
    public void testTwoCommercialOrderLinesAreDifferentWhenTheyHaveDifferentUuid() {
        CommercialOrderLineSplit line1 = new CommercialOrderLineSplit("301", null, null, null, null, null, null, null, null, null, null, null);
        CommercialOrderLineSplit line2 = new CommercialOrderLineSplit("401", null, null, null, null, null, null, null, null, null, null, null);

        Assert.assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonCommercialOrderLine() {
        CommercialOrderLineSplit line = new CommercialOrderLineSplit();

        Assert.assertNotEquals(line, new String());
    }
}
