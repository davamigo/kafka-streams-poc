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
 * Unit test for Member domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestMember {

    @Test
    public void testDefaultConstructor() {
        Member member = new Member();

        assertNull(member.getUuid());
        assertNull(member.getFirstName());
        assertNull(member.getLastName());
        assertNotNull(member.getAddresses());
        assertTrue(member.getAddresses().isEmpty());
    }

    @Test
    public void testCompleteConstructor() {
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address("111", "112", "113", "114", "115", "116", "117"));
        Member member = new Member("101", "102", "103", addresses);

        assertEquals("101", member.getUuid());
        assertEquals("102", member.getFirstName());
        assertEquals("103", member.getLastName());
        assertEquals(1, member.getAddresses().size());
        assertEquals("111", member.getAddresses().get(0).getCountry());
    }

    @Test
    public void testTwoMembersAreEqualWhenTheyHaveTheSameUuid() {
        Member member1 = new Member("201", "202", "203", new ArrayList<>());
        Member member2 = new Member("201", "212", "213", new ArrayList<>());

        assertEquals(member1, member2);
        assertNotSame(member1, member2);
    }

    @Test
    public void testTwoMembersAreDifferentWhenTheyHaveDifferentUuid() {
        List<Address> addresses = new ArrayList<>();
        Member member1 = new Member("301", "302", "303", addresses);
        Member member2 = new Member("401", "302", "303", addresses);

        assertNotEquals(member1, member2);
    }

    @Test
    public void testEqualsWithNonMember() {
        Member member = new Member();

        assertNotEquals(member, new String());
    }
}
