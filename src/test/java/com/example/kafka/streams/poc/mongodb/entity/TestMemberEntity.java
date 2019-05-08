package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for member mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestMemberEntity {

    @Test
    public void testEmptyConstructor() {

        MemberEntity memberEntity = new MemberEntity();

        assertNull(memberEntity.getUuid());
        assertNull(memberEntity.getFirstName());
        assertNull(memberEntity.getLastName());
        assertNotNull(memberEntity.getAddresses());
    }

    @Test
    public void testCopyConstructor() {

        Address address1 = Address.newBuilder().setCountry("ES").build();
        Address address2 = Address.newBuilder().setCountry("US").build();

        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);

        Member member = Member
                .newBuilder()
                .setUuid("101")
                .setFirstName("102")
                .setLastName("103")
                .setAddresses(addresses)
                .build();

        MemberEntity memberEntity = new MemberEntity(member);

        assertEquals("101", memberEntity.getUuid());
        assertEquals("102", memberEntity.getFirstName());
        assertEquals("103", memberEntity.getLastName());
        assertEquals(2, memberEntity.getAddresses().size());
        assertEquals("ES", memberEntity.getAddresses().get(0).getCountry());
        assertEquals("US", memberEntity.getAddresses().get(1).getCountry());
    }
}
