package com.example.kafka.streams.poc.domain.entity.commercialorder;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for commercial order domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrder {
    @Test
    public void testDefaultConstructor() {
        CommercialOrder commercialOrder = new CommercialOrder();

        assertNull(commercialOrder.getUuid());
        assertNotNull(commercialOrder.getDatetime());
        assertNotNull(commercialOrder.getMember());
        assertNotNull(commercialOrder.getShippingAddress());
        assertNull(commercialOrder.getBillingAddress());
        assertNotNull(commercialOrder.getLines());
        assertTrue(commercialOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructorWithNullValues() {

        CommercialOrder commercialOrder = new CommercialOrder(null, null, null, null, null, null);

        assertNull(commercialOrder.getUuid());
        assertNotNull(commercialOrder.getDatetime());
        assertNotNull(commercialOrder.getMember());
        assertNotNull(commercialOrder.getShippingAddress());
        assertNull(commercialOrder.getBillingAddress());
        assertNotNull(commercialOrder.getLines());
        assertTrue(commercialOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructorWithAllValues() {

        Date datetime = new Date();
        Member member = Member.newBuilder().setUuid("111").build();
        Address address1 = Address.newBuilder().setZipCode("121").build();
        Address address2 = Address.newBuilder().setZipCode("131").build();
        CommercialOrderLine line = CommercialOrderLine.newBuilder().setUuid("141").build();
        List<CommercialOrderLine> lines = new ArrayList<>();
        lines.add(line);
        CommercialOrder commercialOrder = new CommercialOrder("101", datetime, member, address1, address2, lines);

        assertEquals("101", commercialOrder.getUuid());
        assertEquals(member, commercialOrder.getMember());
        assertEquals(datetime, commercialOrder.getDatetime());
        assertEquals(address1, commercialOrder.getShippingAddress());
        assertEquals(address2, commercialOrder.getBillingAddress());
        assertEquals(1, commercialOrder.getLines().size());
        assertEquals(line, commercialOrder.getLines().get(0));
    }

    @Test
    public void testTwoCommercialOrdersAreEqualWhenTheyHaveTheSameUuid() {
        CommercialOrder commercialOrder1 = new CommercialOrder("201", null, null, null, null, null);
        CommercialOrder commercialOrder2 = new CommercialOrder("201", null, null, null, null, null);

        assertEquals(commercialOrder1, commercialOrder2);
        assertNotSame(commercialOrder1, commercialOrder2);
    }

    @Test
    public void testTwoCommercialOrdersAreDifferentWhenTheyHaveDifferentUuid() {
        CommercialOrder commercialOrder1 = new CommercialOrder("301", null, null, null, null, null);
        CommercialOrder commercialOrder2 = new CommercialOrder("401", null, null, null, null, null);

        assertNotEquals(commercialOrder1, commercialOrder2);
    }

    @Test
    public void testEqualsWithNonCommercialOrder() {
        CommercialOrder commercialOrder = new CommercialOrder();

        assertNotEquals(commercialOrder, new String());
    }
}
