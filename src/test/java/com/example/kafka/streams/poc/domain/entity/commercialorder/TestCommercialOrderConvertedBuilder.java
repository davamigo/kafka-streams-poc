package com.example.kafka.streams.poc.domain.entity.commercialorder;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * Unit test for CommercialOrder.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderConvertedBuilder {

    @Test
    public void testBuilderSettersReturnExpectedResults() {

        CommercialOrderConverted commercialOrderConverted = CommercialOrderConverted
                .newBuilder()
                .setUuid("101")
                .setDatetime(new Date(102L))
                .setMemberUuid("103")
                .setMemberFirstName("104")
                .setMemberLastName("105")
                .setShippingCountry("106")
                .setShippingCity("107")
                .setShippingZipCode("108")
                .setTotalAmount(109f)
                .setTotalQuantity(110)
                .build();

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
    public void testSetCopiesTheContentFromSourceObject() {

        CommercialOrderConverted source = new CommercialOrderConverted(
                "201",
                new Date(202L),
                "203",
                "204",
                "205",
                "206",
                "207",
                "208",
                209f,
                210
        );

        CommercialOrderConverted commercialOrderConverted = CommercialOrderConverted
                .newBuilder()
                .set(source)
                .build();

        Assert.assertEquals("201", commercialOrderConverted.getUuid());
        Assert.assertEquals(202L, commercialOrderConverted.getDatetime().getTime());
        Assert.assertEquals("203", commercialOrderConverted.getMemberUuid());
        Assert.assertEquals("204", commercialOrderConverted.getMemberFirstName());
        Assert.assertEquals("205", commercialOrderConverted.getMemberLastName());
        Assert.assertEquals("206", commercialOrderConverted.getShippingCountry());
        Assert.assertEquals("207", commercialOrderConverted.getShippingCity());
        Assert.assertEquals("208", commercialOrderConverted.getShippingZipCode());
        Assert.assertEquals(209f, commercialOrderConverted.getTotalAmount(), 0.001);
        Assert.assertEquals(210, commercialOrderConverted.getTotalQuantity());
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted sourceCommercialOrderConverted =
                new com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted(
                        "301",
                        302L,
                        "303",
                        "304",
                        "305",
                        "306",
                        "307",
                        "308",
                        309f,
                        310
                );

        CommercialOrderConverted commercialOrderConverted = CommercialOrderConverted
                .newBuilder()
                .set(sourceCommercialOrderConverted)
                .build();

        Assert.assertEquals("301", commercialOrderConverted.getUuid());
        Assert.assertEquals(302L, commercialOrderConverted.getDatetime().getTime());
        Assert.assertEquals("303", commercialOrderConverted.getMemberUuid());
        Assert.assertEquals("304", commercialOrderConverted.getMemberFirstName());
        Assert.assertEquals("305", commercialOrderConverted.getMemberLastName());
        Assert.assertEquals("306", commercialOrderConverted.getShippingCountry());
        Assert.assertEquals("307", commercialOrderConverted.getShippingCity());
        Assert.assertEquals("308", commercialOrderConverted.getShippingZipCode());
        Assert.assertEquals(309f, commercialOrderConverted.getTotalAmount(), 0.001);
        Assert.assertEquals(310, commercialOrderConverted.getTotalQuantity());
    }
}
