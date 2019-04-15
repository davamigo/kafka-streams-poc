package com.example.kafka.streams.poc.service.member;

import com.example.kafka.streams.poc.schemas.member.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test for RandomMemberSelector service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestRandomMemberSelector {

    @Test
    public void testRandomMemberSelectorReturnAMember() {
        RandomMemberSelector service = new RandomMemberSelector();

        Member member = service.selectMember();

        assertNotNull(member);
        assertNotNull(member.getUuid());
        assertNotNull(member.getFistName());
        assertNotNull(member.getLastName());
        assertNotNull(member.getAddresses());
        assertFalse(member.getAddresses().isEmpty());
    }

    @Test
    public void testRandomMemberSelectorReturnTheSameMemberWhenMaxMemberIs1() {
        RandomMemberSelector service = new RandomMemberSelector(1);

        Member member1 = service.selectMember();
        Member member2 = service.selectMember();

        assertEquals(member1.getUuid(), member2.getUuid());
    }

    @Test
    public void testRandomMemberSelectorReturnTheDifferentMemberWhenMaxMemberIsBig() {
        RandomMemberSelector service = new RandomMemberSelector(Integer.MAX_VALUE);

        Member member1 = service.selectMember();
        Member member2 = service.selectMember();

        assertNotEquals(member1.getUuid(), member2.getUuid());
    }
}
