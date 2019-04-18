package com.example.kafka.streams.poc.domain.entity.address;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for Address domain entity
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestAddress {

    @Test
    public void testInitialState() {
        Address address = new Address();

        assertNull(address.getCountry());
        assertNull(address.getState());
        assertNull(address.getCity());
        assertNull(address.getZipCode());
        assertNull(address.getStreet());
        assertNull(address.getNumber());
        assertNull(address.getExtra());
    }

    @Test
    public void testCompleteConstructor() {
        Address address = new Address("101", "102", "103", "104", "105", "106", "107");

        assertEquals("101", address.getCountry());
        assertEquals("102", address.getState());
        assertEquals("103", address.getCity());
        assertEquals("104", address.getZipCode());
        assertEquals("105", address.getStreet());
        assertEquals("106", address.getNumber());
        assertEquals("107", address.getExtra());
    }

    @Test
    public void testTwoAddressesAreEqualsWhenTheHaveAllTheSameAttributes() {
        Address address1 = new Address("201", "202", "203", "204", "205", "206", "207");
        Address address2 = new Address("201", "202", "203", "204", "205", "206", "207");

        assertEquals(address1, address2);
        assertNotSame(address1, address2);
    }

    @Test
    public void testTwoAddressesAreDifferentWhenTheHAveAnyDifferentAttribute() {
        Address address1 = new Address("301", "302", "303", "304", "305", "306", "307");
        Address address2 = new Address("301", "302", "303", "304", "305", "306", "407");

        assertNotEquals(address1, address2);
    }

    @Test
    public void testEqualsWithNonAddress() {
        Address address = new Address();

        assertNotEquals(address, new String());
    }
}
