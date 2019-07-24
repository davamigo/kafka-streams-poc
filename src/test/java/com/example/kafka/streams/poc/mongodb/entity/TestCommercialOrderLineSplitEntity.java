package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


/**
 * Unit test for commercial order line split mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderLineSplitEntity {

    @Test
    public void testEmptyConstructor() {

        CommercialOrderLineSplitEntity line = new CommercialOrderLineSplitEntity();

        Assert.assertNull(line.getUuid());
        Assert.assertNull(line.getCommercialOrderUuid());
        Assert.assertNull(line.getCommercialOrderDatetime());
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
    public void testCopyConstructor() {

        CommercialOrderLineSplit source = new CommercialOrderLineSplit(
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

        CommercialOrderLineSplitEntity line = new CommercialOrderLineSplitEntity(source);

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
}
