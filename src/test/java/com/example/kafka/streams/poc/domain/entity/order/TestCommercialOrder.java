package com.example.kafka.streams.poc.domain.entity.order;

import com.example.kafka.streams.poc.domain.entity.address.Address;
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
        assertNull(commercialOrder.getDatetime());
        assertNull(commercialOrder.getMemberUuid());
        assertNull(commercialOrder.getShippingAddress());
        assertNull(commercialOrder.getBillingAddress());
        assertNotNull(commercialOrder.getLines());
        assertTrue(commercialOrder.getLines().isEmpty());
    }

    @Test
    public void testCompleteConstructor() {

        Date datetime = new Date();
        Address address1 = Address.newBuilder().setZipCode("111").build();
        Address address2 = Address.newBuilder().setZipCode("121").build();
        List<CommercialOrderLine> lines = new ArrayList<>();
        lines.add(CommercialOrderLine.newBuilder().setUuid("131").build());
        CommercialOrder commercialOrder = new CommercialOrder("101", datetime, "102", address1, address2, lines);

        assertEquals("101", commercialOrder.getUuid());
        assertEquals("102", commercialOrder.getMemberUuid());
        assertEquals(datetime, commercialOrder.getDatetime());
        assertEquals(address1, commercialOrder.getShippingAddress());
        assertEquals(address2, commercialOrder.getBillingAddress());
        assertEquals(1, commercialOrder.getLines().size());
        assertEquals("131", commercialOrder.getLines().get(0).getUuid());
    }

    @Test
    public void testTwoCommercialOrdersAreEqualWhenTheyHaveTheSameUuid() {
        CommercialOrder commercialOrder1 = new CommercialOrder("201", null, "202", null, null, new ArrayList<>());
        CommercialOrder commercialOrder2 = new CommercialOrder("201", null, "212", null, null, new ArrayList<>());

        assertEquals(commercialOrder1, commercialOrder2);
        assertNotSame(commercialOrder1, commercialOrder2);
    }

    @Test
    public void testTwoCommercialOrdersAreDifferentWhenTheyHaveDifferentUuid() {
        CommercialOrder commercialOrder1 = new CommercialOrder("301", null, null, null, null, new ArrayList<>());
        CommercialOrder commercialOrder2 = new CommercialOrder("401", null, null, null, null, new ArrayList<>());

        assertNotEquals(commercialOrder1, commercialOrder2);
    }

    @Test
    public void testEqualsWithNonCommercialOrder() {
        CommercialOrder commercialOrder = new CommercialOrder();

        assertNotEquals(commercialOrder, new String());
    }
}
