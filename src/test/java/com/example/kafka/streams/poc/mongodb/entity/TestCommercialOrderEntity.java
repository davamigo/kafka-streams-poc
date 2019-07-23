package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for commercial order mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderEntity {

    @Test
    public void testEmptyConstructor() {

        CommercialOrderEntity commercialOrderEntity = new CommercialOrderEntity();

        assertNull(commercialOrderEntity.getUuid());
        assertNull(commercialOrderEntity.getDatetime());
        assertNull(commercialOrderEntity.getMemberUuid());
        assertNull(commercialOrderEntity.getShippingAddress());
        assertNull(commercialOrderEntity.getBillingAddress());
        assertNotNull(commercialOrderEntity.getLines());
    }

    @Test
    public void testCopyConstructor() {

        Date date = new Date();

        Member member = Member.newBuilder().setUuid("111").build();
        Address shippingAddress = Address.newBuilder().setCountry("ES").build();
        Address billingAddress = Address.newBuilder().setCountry("US").build();
        List<CommercialOrderLine> lines = new ArrayList<>();
        CommercialOrderLine line = CommercialOrderLine.newBuilder().setUuid("121").build();
        lines.add(line);
        CommercialOrder commercialOrder = new CommercialOrder("101", date, member, shippingAddress, billingAddress, lines);
        CommercialOrderEntity commercialOrderEntity = new CommercialOrderEntity(commercialOrder);

        assertEquals("101", commercialOrderEntity.getUuid());
        assertEquals(date, commercialOrderEntity.getDatetime());
        assertEquals("111", commercialOrderEntity.getMemberUuid());
        assertEquals("ES", commercialOrderEntity.getShippingAddress().getCountry());
        assertEquals("US", commercialOrderEntity.getBillingAddress().getCountry());
        assertEquals("121", commercialOrderEntity.getLines().get(0).getUuid());
    }
}
