package com.example.kafka.streams.poc.service.generator.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.service.generator.address.RandomAddressGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for RandomMemberSelector service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestReusableMemberSelector {

    @Test
    public void testGetMemberReturnMemberWithRandomData() {

        RandomAddressGenerator addressGenerator = new RandomAddressGenerator();
        RandomMemberGenerator memberGenerator = new RandomMemberGenerator(addressGenerator);
        ReusableMemberSelector memberSelector = new ReusableMemberSelector(memberGenerator);
        Member member = memberSelector.getMember();

        assertNotNull(member);
    }

    @Test
    public void testGetMemberReturnTheSameMemberWhenMaxMemberIs1() {

        RandomAddressGenerator addressGenerator = new RandomAddressGenerator();
        RandomMemberGenerator memberGenerator = new RandomMemberGenerator(addressGenerator);
        ReusableMemberSelector memberSelector = new ReusableMemberSelector(memberGenerator, 1);

        Member member1 = memberSelector.getMember();
        Member member2 = memberSelector.getMember();

        assertEquals(member1, member2);
        assertNotSame(member1, member2);
    }

    @Test
    public void testGetMemberReturnTheDifferentMemberWhenMaxMemberIsBig() {

        RandomAddressGenerator addressGenerator = new RandomAddressGenerator();
        RandomMemberGenerator memberGenerator = new RandomMemberGenerator(addressGenerator);
        ReusableMemberSelector memberSelector = new ReusableMemberSelector(memberGenerator, Integer.MAX_VALUE);

        Member member1 = memberSelector.getMember();
        Member member2 = memberSelector.getMember();

        assertNotEquals(member1, member2);
    }
}
