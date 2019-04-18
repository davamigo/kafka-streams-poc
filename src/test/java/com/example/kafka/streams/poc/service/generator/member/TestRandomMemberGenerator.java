package com.example.kafka.streams.poc.service.generator.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.service.generator.address.RandomAddressGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(member.getAddresses().size() < 4);
    }
}
