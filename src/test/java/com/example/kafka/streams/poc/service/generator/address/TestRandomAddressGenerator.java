package com.example.kafka.streams.poc.service.generator.address;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;

/**
 * Unit test for RandomAddressGenerator service
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestRandomAddressGenerator {

    @Test
    public void testRandomMemberSelectorReturnAMember() {

        RandomAddressGenerator service = new RandomAddressGenerator();
        Address address = service.getAddress();

        assertNotNull(address);
        assertNotNull(address.getCountry());
        assertNotNull(address.getState());
        assertNotNull(address.getCity());
        assertNotNull(address.getZipCode());
        assertNotNull(address.getStreet());
        assertNotNull(address.getNumber());
    }
}
