package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for converted commercial order mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderConvertedEntity {

    @Test
    public void testEmptyConstructor() {

        CommercialOrderConvertedEntity order = new CommercialOrderConvertedEntity();

        Assert.assertNull(order.getUuid());
        Assert.assertNull(order.getDatetime());
        Assert.assertNull(order.getMemberUuid());
        Assert.assertNull(order.getMemberFirstName());
        Assert.assertNull(order.getMemberLastName());
        Assert.assertNull(order.getShippingCountry());
        Assert.assertNull(order.getShippingCity());
        Assert.assertNull(order.getShippingZipCode());
        Assert.assertEquals(0f, order.getTotalAmount(), 0.001);
        Assert.assertEquals(0, order.getTotalQuantity());
    }

    @Test
    public void testCopyConstructor() {

        CommercialOrderConverted commercialOrder = new CommercialOrderConverted(
                "101",
                new Date(102L),
                "103",
                "104",
                "105",
                "106",
                "107",
                "108",
                109f,
                110
        );

        CommercialOrderConvertedEntity order = new CommercialOrderConvertedEntity(commercialOrder);

        Assert.assertEquals("101", order.getUuid());
        Assert.assertEquals(102L, order.getDatetime().getTime());
        Assert.assertEquals("103", order.getMemberUuid());
        Assert.assertEquals("104", order.getMemberFirstName());
        Assert.assertEquals("105", order.getMemberLastName());
        Assert.assertEquals("106", order.getShippingCountry());
        Assert.assertEquals("107", order.getShippingCity());
        Assert.assertEquals("108", order.getShippingZipCode());
        Assert.assertEquals(109f, order.getTotalAmount(), 0.001);
        Assert.assertEquals(110, order.getTotalQuantity());
    }
}
