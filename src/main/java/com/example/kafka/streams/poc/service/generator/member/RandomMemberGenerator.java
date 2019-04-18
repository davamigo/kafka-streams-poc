package com.example.kafka.streams.poc.service.generator.member;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.service.generator.address.AddressGeneratorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Service to get a member for generating testing data
 */
@Component
public class RandomMemberGenerator implements MemberGeneratorInterface {

    /** Service to generate the addresses */
    private AddressGeneratorInterface addressesGenerator;

    /** Array of first names */
    private static final String[] firstNames = {
            "John",
            "Hannibal",
            "Brad",
            "George",
            "Peter",
            "Harry",
            "Robert",
            "Steve"
    };

    /** Array of last names */
    private static final String[] lastNames = {
            "Smith",
            "Pitt",
            "Potter",
            "Harris",
            "Jackson",
            "Martin"
    };

    /**
     * Autowired constructor
     *
     * @param addressesGenerator the service to generate the addresses
     */
    @Autowired
    public RandomMemberGenerator(AddressGeneratorInterface addressesGenerator) {
        this.addressesGenerator = addressesGenerator;
    }

    /**
     * Generate a member with random data
     *
     * @return a member with random data
     */
    @Override
    public synchronized Member getMember() {

        Member.Builder builder = Member.newBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setFirstName(createRandomFirstName())
                .setLastName(createRandomLastName());

        // Add 1 to 3 addresses
        int num = 1 + ((new Random()).nextInt(3));
        List<Address> addresses = new ArrayList<>();
        while (num-- > 0) {
            addresses.add(createRandomAddress());
        }
        builder.setAddresses(addresses);

        return builder.build();
    }

    /**
     * Selects a random first name from the list
     *
     * @return a random first name
     */
    private String createRandomFirstName() {
        return firstNames[(new Random()).nextInt(firstNames.length)];
    }

    /**
     * Selects a random last name from the list
     *
     * @return a random last name
     */
    private String createRandomLastName() {
        return lastNames[(new Random()).nextInt(lastNames.length)];
    }

    /**
     * Selects a random address from the list
     *
     * @return a random address
     */
    private Address createRandomAddress() {
        return addressesGenerator.getAddress();
    }
}
