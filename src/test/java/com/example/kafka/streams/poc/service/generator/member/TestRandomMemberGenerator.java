package com.example.kafka.streams.poc.service.generator.member;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.service.generator.address.RandomAddressGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for RandomMemberGenerator service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestRandomMemberGenerator {

    @Test
    public void testGetMemberReturnMemberWithRandomData() {

        RandomMemberGenerator service = new RandomMemberGenerator(new RandomAddressGenerator());
        Member member = service.getMember();

        assertNotNull(member);
        assertNotNull(member.getUuid());
        assertNotNull(member.getFirstName());
        assertNotNull(member.getLastName());
        assertNotNull(member.getAddresses());
        assertTrue(member.getAddresses().size() > 0);
        assertTrue(member.getAddresses().size() < 3);
    }

    @Test
    public void testWhenTwoAddressesBothAreFromTheSameCountry() {

        RandomMemberGenerator service = new RandomMemberGenerator(new RandomAddressGenerator());
        Member member = service.getMember();
        while (member.getAddresses().size() < 2) {
            member = service.getMember();
        }

        List<Address> addresses = member.getAddresses();
        Address address1 = addresses.get(0);
        Address address2 = addresses.get(1);

        assertEquals(address1.getCountry(), address2.getCountry());
    }
}
