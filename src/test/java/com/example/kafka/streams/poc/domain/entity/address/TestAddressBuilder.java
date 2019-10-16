package com.example.kafka.streams.poc.domain.entity.address;

import com.example.kafka.streams.poc.schemas.member.MemberAddress;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for Address.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestAddressBuilder {

    @Test
    public void testBuilderSettersReturnExpectedResults() {

        Address.Builder builder = Address.newBuilder();
        Address address = builder.setCountry("101")
                .setState("102")
                .setCity("103")
                .setZipCode("104")
                .setStreet("105")
                .setNumber("106")
                .setExtra("107")
                .build();

        assertEquals("101", address.getCountry());
        assertEquals("102", address.getState());
        assertEquals("103", address.getCity());
        assertEquals("104", address.getZipCode());
        assertEquals("105", address.getStreet());
        assertEquals("106", address.getNumber());
        assertEquals("107", address.getExtra());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        Address source = new Address("201", "202", "203", "204", "205", "206", "207");

        Address.Builder builder = Address.newBuilder().set(source);
        Address address = builder.build();

        assertEquals(source, address);
        assertNotSame(source, address);
    }

    @Test
    public void testSetCopiesTheContentFromMemberAddressAvroSource() {

        MemberAddress memberAddress = MemberAddress.newBuilder()
                .setCountry("301")
                .setState("302")
                .setCity("303")
                .setZipCode("304")
                .setStreet("305")
                .setNumber("306")
                .setExtra("307")
                .build();

        Address.Builder builder = Address.newBuilder().set(memberAddress);
        Address address = builder.build();

        assertEquals("301", address.getCountry());
        assertEquals("302", address.getState());
        assertEquals("303", address.getCity());
        assertEquals("304", address.getZipCode());
        assertEquals("305", address.getStreet());
        assertEquals("306", address.getNumber());
        assertEquals("307", address.getExtra());
    }

    @Test
    public void testSetCopiesTheContentFromCommercialOrderAddressAvroSource() {

        CommercialOrderAddress commercialOrderAddress = CommercialOrderAddress.newBuilder()
                .setCountry("401")
                .setState("402")
                .setCity("403")
                .setZipCode("404")
                .setStreet("405")
                .setNumber("406")
                .setExtra("407")
                .build();

        Address.Builder builder = Address.newBuilder();
        builder.set(commercialOrderAddress);
        Address address = builder.build();

        assertEquals("401", address.getCountry());
        assertEquals("402", address.getState());
        assertEquals("403", address.getCity());
        assertEquals("404", address.getZipCode());
        assertEquals("405", address.getStreet());
        assertEquals("406", address.getNumber());
        assertEquals("407", address.getExtra());
    }
}
