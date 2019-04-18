package com.example.kafka.streams.poc.domain.entity.order;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for Member.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestCommercialOrderExporter {

    @Test
    public void testExport() {

        Date datetime = new Date();

        Member member = Member.newBuilder()
                .setUuid("111")
                .build();

        Address address1 = Address.newBuilder()
                .setCountry("121")
                .setCity("122")
                .setZipCode("123")
                .build();

        Address address2 = Address.newBuilder()
                .setCountry("131")
                .setCity("132")
                .setZipCode("133")
                .build();

        CommercialOrderLine commercialOrderLine1 = CommercialOrderLine.newBuilder()
                .setUuid("141")
                .setCommercialOrderUuid("142")
                .setProduct(Product.newBuilder().setUuid("433").build())
                .setPrice(434f)
                .setQuantity(435)
                .build();

        CommercialOrderLine commercialOrderLine2 = CommercialOrderLine.newBuilder()
                .setUuid("151")
                .setCommercialOrderUuid("152")
                .setProduct(Product.newBuilder().setUuid("143").build())
                .setPrice(154f)
                .setQuantity(155)
                .build();

        CommercialOrder commercialOrder = CommercialOrder.newBuilder()
                .setUuid("101")
                .setDatetime(datetime)
                .setMember(member)
                .setShippingAddress(address1)
                .setBillingAddress(address2)
                .addLine(commercialOrderLine1)
                .addLine(commercialOrderLine2)
                .build();

        com.example.kafka.streams.poc.schemas.order.CommercialOrder avroCommercialOrder
                = CommercialOrder.newAvroExporter(commercialOrder).export();

        assertEquals("101", avroCommercialOrder.getUuid());
        assertEquals("111", avroCommercialOrder.getMemberUuid());
        assertEquals(datetime.getTime(), avroCommercialOrder.getDatetime().longValue());
        assertEquals("121", avroCommercialOrder.getShippingAddress().getCountry());
        assertEquals("131", avroCommercialOrder.getBillingAddress().getCountry());
        assertEquals(2, avroCommercialOrder.getLines().size());
        assertEquals("141", avroCommercialOrder.getLines().get(0).getUuid());
        assertEquals("151", avroCommercialOrder.getLines().get(1).getUuid());
    }
}
