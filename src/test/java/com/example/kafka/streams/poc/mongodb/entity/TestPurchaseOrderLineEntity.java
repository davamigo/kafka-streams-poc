package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for purchase order line mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestPurchaseOrderLineEntity {

    @Test
    public void testEmptyConstructor() {

        PurchaseOrderLineEntity lineEntity = new PurchaseOrderLineEntity();

        Assert.assertNull(lineEntity.getUuid());
        Assert.assertNull(lineEntity.getAggregationKey());
        Assert.assertNull(lineEntity.getCountry());
        Assert.assertNull(lineEntity.getDate());
        Assert.assertNull(lineEntity.getProductUuid());
        Assert.assertNull(lineEntity.getProductName());
        Assert.assertNull(lineEntity.getProductType());
        Assert.assertNull(lineEntity.getProductBarCode());
        Assert.assertEquals(0f, lineEntity.getProductPrice(), 0.001);
        Assert.assertEquals(0, lineEntity.getQuantity());
    }

    @Test
    public void testCopyConstructor() {

        PurchaseOrderLine source = new PurchaseOrderLine(
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

        PurchaseOrderLineEntity lineEntity = new PurchaseOrderLineEntity(source);

        Assert.assertEquals("101", lineEntity.getUuid());
        Assert.assertEquals("102", lineEntity.getAggregationKey());
        Assert.assertEquals("103", lineEntity.getCountry());
        Assert.assertEquals(104L, lineEntity.getDate().getTime());
        Assert.assertEquals("105", lineEntity.getProductUuid());
        Assert.assertEquals("106", lineEntity.getProductName());
        Assert.assertEquals("107", lineEntity.getProductType());
        Assert.assertEquals("108", lineEntity.getProductBarCode());
        Assert.assertEquals(109f, lineEntity.getProductPrice(), 0.001);
        Assert.assertEquals(110, lineEntity.getQuantity());
    }
}
