package com.example.kafka.streams.poc.domain.entity.member;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for Member.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestMemberBuilder {

    @Test
    public void testBuiderSettersReturnExpectedResults() {

        Member.Builder builder = Member.newBuilder();
        Member member = builder
                .setUuid("101")
                .setFirstName("102")
                .setLastName("103")
                .build();

        assertEquals("101", member.getUuid());
        assertEquals("102", member.getFirstName());
        assertEquals("103", member.getLastName());
        assertTrue(member.getAddresses().isEmpty());
    }

    @Test
    public void testSetCopiesTheContentFromSourceObject() {

        Address address = Address.newBuilder().setCountry("211").build();
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        Member source = new Member("201", "202", "203", addresses);
        Member.Builder builder = Member.newBuilder().set(source);
        Member member = builder.build();

        assertEquals(source, member);
        assertNotSame(source, member);
    }

    @Test
    public void testSetCopiesTheContentFromAvroSource() {

        com.example.kafka.streams.poc.schemas.member.MemberAddress sourceAddress =
                com.example.kafka.streams.poc.schemas.member.MemberAddress.newBuilder()
                        .setCountry("311")
                        .setCity("312")
                        .setZipCode("313")
                        .build();

        List<com.example.kafka.streams.poc.schemas.member.MemberAddress> sourceAddresses = new ArrayList<>();
        sourceAddresses.add(sourceAddress);

        com.example.kafka.streams.poc.schemas.member.Member sourceMember =
                com.example.kafka.streams.poc.schemas.member.Member.newBuilder()
                        .setUuid("301")
                        .setFirstName("302")
                        .setLastName("303")
                        .setAddresses(sourceAddresses)
                        .build();

        Member member = Member.newBuilder().set(sourceMember).build();

        assertEquals("301", member.getUuid());
        assertEquals("302", member.getFirstName());
        assertEquals("303", member.getLastName());
        assertEquals(1, member.getAddresses().size());
        assertEquals("311", member.getAddresses().get(0).getCountry());
        assertEquals("312", member.getAddresses().get(0).getCity());
        assertEquals("313", member.getAddresses().get(0).getZipCode());
    }

    @Test
    public void testSetAddresses() {

        Address address1 = Address.newBuilder().setCountry("411").build();
        Address address2 = Address.newBuilder().setCountry("411").build();
        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);

        Member.Builder builder = Member.newBuilder();
        Member member = builder
                .setAddresses(addresses)
                .build();

        assertEquals(2, member.getAddresses().size());
        assertEquals(address1, member.getAddresses().get(0));
        assertEquals(address2, member.getAddresses().get(1));
    }

    @Test
    public void testAddAddresses() {

        Address address1 = Address.newBuilder().setCountry("411").build();
        Address address2 = Address.newBuilder().setCountry("411").build();

        Member.Builder builder = Member.newBuilder();
        Member member = builder
                .addAddress(address1)
                .addAddress(address2)
                .build();

        assertEquals(2, member.getAddresses().size());
        assertEquals(address1, member.getAddresses().get(0));
        assertEquals(address2, member.getAddresses().get(1));
    }

    @Test
    public void testClearAddress() {

        Member member = Member.newBuilder()
                .addAddress(new Address())
                .clearAddress()
                .build();

        assertTrue(member.getAddresses().isEmpty());
    }
}
