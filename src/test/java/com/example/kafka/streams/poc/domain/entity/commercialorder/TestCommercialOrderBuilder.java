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
 * Unit test for CommercialOrder.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderBuilder {

    @Test
    public void testBuiderSettersReturnExpectedResults() {

        Date datetime = new Date();
        Member member = Member.newBuilder().setUuid("111").build();
        Address address1 = Address.newBuilder().setZipCode("121").build();
        Address address2 = Address.newBuilder().setZipCode("131").build();

        CommercialOrder.Builder builder = CommercialOrder.newBuilder();
        CommercialOrder commercialOrder = builder
                .setUuid("101")
                .setDatetime(datetime)
                .setMember(member)
                .setShippingAddress(address1)
                .setBillingAddress(address2)
                .build();

        assertEquals("101", commercialOrder.getUuid());
        assertEquals(member, commercialOrder.getMember());
        assertEquals(datetime, commercialOrder.getDatetime());
        assertEquals(address1, commercialOrder.getShippingAddress());
        assertEquals(address2, commercialOrder.getBillingAddress());
        assertTrue(commercialOrder.getLines().isEmpty());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        Date datetime = new Date();
        Member member = Member.newBuilder().setUuid("211").build();
        Address address1 = Address.newBuilder().setZipCode("221").build();
        Address address2 = Address.newBuilder().setZipCode("231").build();
        CommercialOrderLine line = CommercialOrderLine.newBuilder().setUuid("241").build();
        List<CommercialOrderLine> lines = new ArrayList<>();
        lines.add(line);

        CommercialOrder source = new CommercialOrder("201", datetime, member, address1, address2, lines);
        CommercialOrder.Builder builder = CommercialOrder.newBuilder().set(source);
        CommercialOrder commercialOrder = builder.build();

        assertEquals(source, commercialOrder);
        assertNotSame(source, commercialOrder);
        assertEquals("201", commercialOrder.getUuid());
        assertEquals(member, commercialOrder.getMember());
        assertEquals(datetime, commercialOrder.getDatetime());
        assertEquals(address1, commercialOrder.getShippingAddress());
        assertEquals(address2, commercialOrder.getBillingAddress());
        assertEquals(1, commercialOrder.getLines().size());
        assertEquals(line, commercialOrder.getLines().get(0));
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        Date datetime = new Date();
        long sourceDatetime = datetime.getTime();

        com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress sourceAddress1
                = com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress.newBuilder()
                        .setCountry("311")
                        .setCity("312")
                        .setZipCode("313")
                        .build();

        com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress sourceAddress2
                = com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress.newBuilder()
                        .setCountry("321")
                        .setCity("322")
                        .setZipCode("323")
                        .build();

        com.example.kafka.streams.poc.schemas.order.CommercialOrderLine sourceLine1
                = com.example.kafka.streams.poc.schemas.order.CommercialOrderLine.newBuilder()
                        .setUuid("331")
                        .setCommercialOrderUuid("332")
                        .setProductUuid("333")
                        .setPrice(334f)
                        .setQuantity(335)
                        .build();

        com.example.kafka.streams.poc.schemas.order.CommercialOrderLine sourceLine2
                = com.example.kafka.streams.poc.schemas.order.CommercialOrderLine.newBuilder()
                        .setUuid("341")
                        .setCommercialOrderUuid("342")
                        .setProductUuid("343")
                        .setPrice(344f)
                        .setQuantity(345)
                        .build();

        List<com.example.kafka.streams.poc.schemas.order.CommercialOrderLine> sourceLines = new ArrayList<>();
        sourceLines.add(sourceLine1);
        sourceLines.add(sourceLine2);

        com.example.kafka.streams.poc.schemas.order.CommercialOrder sourceCommercialOrder
                = new com.example.kafka.streams.poc.schemas.order.CommercialOrder("301", sourceDatetime, "302", sourceAddress1, sourceAddress2, sourceLines);

        CommercialOrder.Builder builder = CommercialOrder.newBuilder().set(sourceCommercialOrder);
        CommercialOrder commercialOrder = builder.build();

        assertEquals("301", commercialOrder.getUuid());
        assertEquals("302", commercialOrder.getMember().getUuid());
        assertEquals(datetime, commercialOrder.getDatetime());

        assertEquals("311", commercialOrder.getShippingAddress().getCountry());
        assertEquals("312", commercialOrder.getShippingAddress().getCity());
        assertEquals("313", commercialOrder.getShippingAddress().getZipCode());

        assertEquals("321", commercialOrder.getBillingAddress().getCountry());
        assertEquals("322", commercialOrder.getBillingAddress().getCity());
        assertEquals("323", commercialOrder.getBillingAddress().getZipCode());

        assertEquals(2, commercialOrder.getLines().size());
        assertEquals("331", commercialOrder.getLines().get(0).getUuid());
        assertEquals("341", commercialOrder.getLines().get(1).getUuid());
    }

    @Test
    public void testSetLines() {

        CommercialOrderLine commercialOrderLine1 = CommercialOrderLine.newBuilder().setUuid("411").build();
        CommercialOrderLine commercialOrderLine2 = CommercialOrderLine.newBuilder().setUuid("421").build();
        List<CommercialOrderLine> lines = new ArrayList<>();
        lines.add(commercialOrderLine1);
        lines.add(commercialOrderLine2);

        CommercialOrder.Builder builder = CommercialOrder.newBuilder();
        CommercialOrder commercialOrder = builder
                .setLines(lines)
                .build();

        assertEquals(2, commercialOrder.getLines().size());
        assertEquals(commercialOrderLine1, commercialOrder.getLines().get(0));
        assertEquals(commercialOrderLine2, commercialOrder.getLines().get(1));
    }

    @Test
    public void testAddLines() {

        CommercialOrderLine commercialOrderLine1 = CommercialOrderLine.newBuilder().setUuid("521").build();
        CommercialOrderLine commercialOrderLine2 = CommercialOrderLine.newBuilder().setUuid("521").build();

        CommercialOrder.Builder builder = CommercialOrder.newBuilder();
        CommercialOrder commercialOrder = builder
                .addLine(commercialOrderLine1)
                .addLine(commercialOrderLine2)
                .build();

        assertEquals(2, commercialOrder.getLines().size());
        assertEquals(commercialOrderLine1, commercialOrder.getLines().get(0));
        assertEquals(commercialOrderLine2, commercialOrder.getLines().get(1));
    }

    @Test
    public void testClearCommercialOrderLine() {

        CommercialOrder commercialOrder = CommercialOrder.newBuilder()
                .addLine(new CommercialOrderLine())
                .clearLines()
                .build();

        assertTrue(commercialOrder.getLines().isEmpty());
    }
}
