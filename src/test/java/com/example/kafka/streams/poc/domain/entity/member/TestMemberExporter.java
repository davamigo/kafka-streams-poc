package com.example.kafka.streams.poc.domain.entity.member;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for Member.Builder class
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestMemberExporter {

    @Test
    public void testExport() {

        Member member = Member.newBuilder()
                .setUuid("101")
                .setFirstName("102")
                .setLastName("103")
                .addAddress(Address.newBuilder().setCountry("111").setCity("112").setZipCode("113").build())
                .build();

        com.example.kafka.streams.poc.schemas.member.Member avroMember = Member.newAvroExporter(member).export();

        assertEquals("101", avroMember.getUuid());
        assertEquals("102", avroMember.getFirstName());
        assertEquals("103", avroMember.getLastName());
        assertEquals(1, avroMember.getAddresses().size());
        assertEquals("111", avroMember.getAddresses().get(0).getCountry());
        assertEquals("112", avroMember.getAddresses().get(0).getCity());
        assertEquals("113", avroMember.getAddresses().get(0).getZipCode());
    }
}
