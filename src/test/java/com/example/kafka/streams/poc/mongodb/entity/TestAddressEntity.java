package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test for address mongoDB entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestAddressEntity {

    @Test
    public void testEmptyConstructor() {

        AddressEntity addressEntity = new AddressEntity();

        assertNull(addressEntity.getCountry());
        assertNull(addressEntity.getState());
        assertNull(addressEntity.getCity());
        assertNull(addressEntity.getZipCode());
        assertNull(addressEntity.getStreet());
        assertNull(addressEntity.getNumber());
        assertNull(addressEntity.getExtra());
    }

    @Test
    public void testCopyConstructor() {

        Address address = new Address("101", "102", "103", "104", "105", "106", "107");
        AddressEntity addressEntity = new AddressEntity(address);

        assertEquals("101", addressEntity.getCountry());
        assertEquals("102", addressEntity.getState());
        assertEquals("103", addressEntity.getCity());
        assertEquals("104", addressEntity.getZipCode());
        assertEquals("105", addressEntity.getStreet());
        assertEquals("106", addressEntity.getNumber());
        assertEquals("107", addressEntity.getExtra());
    }
}
