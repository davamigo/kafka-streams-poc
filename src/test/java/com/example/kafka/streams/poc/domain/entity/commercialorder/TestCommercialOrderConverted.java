package com.example.kafka.streams.poc.domain.entity.commercialorder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for commercial order converted domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderConverted {
    @Test
    public void testDefaultConstructor() {
        CommercialOrderConverted commercialOrderConverted = new CommercialOrderConverted();

        Assert.assertNull(commercialOrderConverted.getUuid());
        Assert.assertNotNull(commercialOrderConverted.getDatetime());
        Assert.assertNull(commercialOrderConverted.getMemberUuid());
        Assert.assertNull(commercialOrderConverted.getMemberFirstName());
        Assert.assertNull(commercialOrderConverted.getMemberLastName());
        Assert.assertNull(commercialOrderConverted.getShippingCountry());
        Assert.assertNull(commercialOrderConverted.getShippingCity());
        Assert.assertNull(commercialOrderConverted.getShippingZipCode());
        Assert.assertEquals(0f, commercialOrderConverted.getTotalAmount(), 0.001);
        Assert.assertEquals(0, commercialOrderConverted.getTotalQuantity());
    }

    @Test
    public void testCompleteConstructorWithNullValues() {

        CommercialOrderConverted commercialOrderConverted = new CommercialOrderConverted(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Assert.assertNull(commercialOrderConverted.getUuid());
        Assert.assertNotNull(commercialOrderConverted.getDatetime());
        Assert.assertNull(commercialOrderConverted.getMemberUuid());
        Assert.assertNull(commercialOrderConverted.getMemberFirstName());
        Assert.assertNull(commercialOrderConverted.getMemberLastName());
        Assert.assertNull(commercialOrderConverted.getShippingCountry());
        Assert.assertNull(commercialOrderConverted.getShippingCity());
        Assert.assertNull(commercialOrderConverted.getShippingZipCode());
        Assert.assertEquals(0f, commercialOrderConverted.getTotalAmount(), 0.001);
        Assert.assertEquals(0, commercialOrderConverted.getTotalQuantity());
    }

    @Test
    public void testCompleteConstructorWithAllValues() {

        CommercialOrderConverted commercialOrderConverted = new CommercialOrderConverted(
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

        Assert.assertEquals("101", commercialOrderConverted.getUuid());
        Assert.assertEquals(102L, commercialOrderConverted.getDatetime().getTime());
        Assert.assertEquals("103", commercialOrderConverted.getMemberUuid());
        Assert.assertEquals("104", commercialOrderConverted.getMemberFirstName());
        Assert.assertEquals("105", commercialOrderConverted.getMemberLastName());
        Assert.assertEquals("106", commercialOrderConverted.getShippingCountry());
        Assert.assertEquals("107", commercialOrderConverted.getShippingCity());
        Assert.assertEquals("108", commercialOrderConverted.getShippingZipCode());
        Assert.assertEquals(109f, commercialOrderConverted.getTotalAmount(), 0.001);
        Assert.assertEquals(110, commercialOrderConverted.getTotalQuantity());
    }

    @Test
    public void testTwoCommercialOrderConvertedsAreEqualWhenTheyHaveTheSameUuid() {
        CommercialOrderConverted commercialOrderConverted1 = new CommercialOrderConverted("201", null, null, null, null, null, null, null, null, null);
        CommercialOrderConverted commercialOrderConverted2 = new CommercialOrderConverted("201", null, null, null, null, null, null, null, null, null);

        Assert.assertEquals(commercialOrderConverted1, commercialOrderConverted2);
        Assert.assertNotSame(commercialOrderConverted1, commercialOrderConverted2);
    }

    @Test
    public void testTwoCommercialOrderConvertedsAreDifferentWhenTheyHaveDifferentUuid() {
        CommercialOrderConverted commercialOrderConverted1 = new CommercialOrderConverted("301", null, null, null, null, null, null, null, null, null);
        CommercialOrderConverted commercialOrderConverted2 = new CommercialOrderConverted("401", null, null, null, null, null, null, null, null, null);

        Assert.assertNotEquals(commercialOrderConverted1, commercialOrderConverted2);
    }

    @Test
    public void testEqualsWithNonCommercialOrderConverted() {
        CommercialOrderConverted commercialOrderConverted = new CommercialOrderConverted();

        Assert.assertNotEquals(commercialOrderConverted, new String());
    }
}
