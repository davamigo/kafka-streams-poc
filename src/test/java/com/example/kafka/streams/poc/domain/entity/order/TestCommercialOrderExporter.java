package com.example.kafka.streams.poc.domain.entity.order;

import com.example.kafka.streams.poc.domain.entity.address.Address;
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
        Address address1 = Address.newBuilder()
                .setCountry("111")
                .setCity("112")
                .setZipCode("113")
                .build();

        Address address2 = Address.newBuilder()
                .setCountry("121")
                .setCity("122")
                .setZipCode("123")
                .build();

        CommercialOrderLine commercialOrderLine1 = CommercialOrderLine.newBuilder()
                .setUuid("131")
                .setCommercialOrderUuid("132")
                .setProductUuid("133")
                .setPrice(134f)
                .setQuantity(135)
                .build();

        CommercialOrderLine commercialOrderLine2 = CommercialOrderLine.newBuilder()
                .setUuid("141")
                .setCommercialOrderUuid("142")
                .setProductUuid("143")
                .setPrice(144f)
                .setQuantity(145)
                .build();

        CommercialOrder commercialOrder = CommercialOrder.newBuilder()
                .setUuid("101")
                .setDatetime(datetime)
                .setMemberUuid("102")
                .setShippingAddress(address1)
                .setBillingAddress(address2)
                .addLine(commercialOrderLine1)
                .addLine(commercialOrderLine2)
                .build();

        com.example.kafka.streams.poc.schemas.order.CommercialOrder avroCommercialOrder
                = CommercialOrder.newAvroExporter(commercialOrder).export();

        assertEquals("101", avroCommercialOrder.getUuid());
        assertEquals("102", avroCommercialOrder.getMemberUuid());
        assertEquals(datetime.getTime(), avroCommercialOrder.getDatetime().longValue());
        assertEquals("111", avroCommercialOrder.getShippingAddress().getCountry());
        assertEquals("121", avroCommercialOrder.getBillingAddress().getCountry());
        assertEquals(2, avroCommercialOrder.getLines().size());
        assertEquals("131", avroCommercialOrder.getLines().get(0).getUuid());
        assertEquals("141", avroCommercialOrder.getLines().get(1).getUuid());
    }
}
