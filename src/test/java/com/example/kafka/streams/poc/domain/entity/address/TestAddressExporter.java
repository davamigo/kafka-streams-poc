package com.example.kafka.streams.poc.domain.entity.address;

import com.example.kafka.streams.poc.schemas.member.MemberAddress;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for Address.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestAddressExporter {

    @Test
    public void testExportToMemberAddress() {

        Address address = new Address("101", "102", "103", "104", "105", "106", "107");

        MemberAddress memberAddress = Address.newAvroExporter(address).exportMemberAddress();

        assertEquals("101", memberAddress.getCountry());
        assertEquals("102", memberAddress.getState());
        assertEquals("103", memberAddress.getCity());
        assertEquals("104", memberAddress.getZipCode());
        assertEquals("105", memberAddress.getStreet());
        assertEquals("106", memberAddress.getNumber());
        assertEquals("107", memberAddress.getExtra());
    }

    @Test
    public void testExportToCommercialOrderAddress() {

        Address address = new Address("201", "202", "203", "204", "205", "206", "207");

        CommercialOrderAddress commercialOrderAddress = Address.newAvroExporter(address).exportCommercialOrderAddress();

        assertEquals("201", commercialOrderAddress.getCountry());
        assertEquals("202", commercialOrderAddress.getState());
        assertEquals("203", commercialOrderAddress.getCity());
        assertEquals("204", commercialOrderAddress.getZipCode());
        assertEquals("205", commercialOrderAddress.getStreet());
        assertEquals("206", commercialOrderAddress.getNumber());
        assertEquals("207", commercialOrderAddress.getExtra());
    }
}
