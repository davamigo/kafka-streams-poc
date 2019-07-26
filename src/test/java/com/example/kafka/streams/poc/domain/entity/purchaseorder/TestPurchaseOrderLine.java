package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for PurchaseOrderLine domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderLine {

    @Test
    public void testDefaultConstructor() {
        PurchaseOrderLine line = new PurchaseOrderLine();

        Assert.assertNull(line.getUuid());
        Assert.assertNull(line.getAggregationKey());
        Assert.assertNull(line.getCountry());
        Assert.assertNotNull(line.getDate());
        Assert.assertNull(line.getProductUuid());
        Assert.assertNull(line.getProductName());
        Assert.assertNull(line.getProductType());
        Assert.assertNull(line.getProductBarCode());
        Assert.assertEquals(0f, line.getProductPrice(), 0.001);
        Assert.assertEquals(1, line.getQuantity());
    }

    @Test
    public void testCompleteConstructor() {
        PurchaseOrderLine line = new PurchaseOrderLine(
                "101",
                "102",
                "103",
                new Date(104L),
                "105",
                "106",
                "107",
                "108",
                109f,
                110
        );

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
    public void testTwoLinesAreEqualWhenTheyHaveTheSameKey() {
        PurchaseOrderLine line1 = new PurchaseOrderLine("201", "202", "203", new Date(204L), "205", "206", "207", "208", 209f, 210);
        PurchaseOrderLine line2 = new PurchaseOrderLine("201", null, null, null, null, null, null, null, null, null);

        Assert.assertEquals(line1, line2);
        Assert.assertNotSame(line1, line2);
    }

    @Test
    public void testTwoPurchaseOrderLinesAreDifferentWhenTheyHaveDifferentKey() {
        PurchaseOrderLine line1 = new PurchaseOrderLine("301", "302", "303", new Date(304L), "305", "306", "307", "308", 309f, 310);
        PurchaseOrderLine line2 = new PurchaseOrderLine("401", "302", "303", new Date(304L), "305", "306", "307", "308", 309f, 310);

        Assert.assertNotEquals(line1, line2);
    }

    @Test
    public void testEqualsWithNonPurchaseOrderLine() {
        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();

        Assert.assertNotEquals(purchaseOrderLine, new String());
    }
}
