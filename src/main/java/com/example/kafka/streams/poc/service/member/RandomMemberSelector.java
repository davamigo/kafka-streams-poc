package com.example.kafka.streams.poc.service.member;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.member.MemberAddress;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Service to select a random member
 */
@Component
public class RandomMemberSelector {

    /** List of members created in this session */
    private List<Member> createdMembers;

    /** Max. members allowed in the list */
    private int maxMembers;

    /** Default max. members in the list */
    private static final int DEFAULT_MAX_MEMBERS = 100;

    /**
     * Array of first names
     */
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

    /**
     * Array of last names
     */
    private static final String[] lastNames = {
            "Smith",
            "Pitt",
            "Potter",
            "Harris",
            "Jackson",
            "Martin"
    };

    /**
     * Array of addresses
     */
    private static final String[][] addresses = {
            {"US", "Florida",      "Port St Lucie", "33452", "Elkview Drive", "1526", null},
            {"US", "Tennessee",    "Memphis",       "38110", "Edgewood Road", "133",  null},
            {"US", "Pennsylvania", "Pittsburgh",    "17101", "Lincoln Drive", "3455", null},
            {"US", "Pennsylvania", "Harrisburg",    "15222", "Jacobs Street", "2173", null},
            {"US", "Kentucky",     "Fairplay",      "42735", "Glen Street",   "2841", null},
    };

    /**
     * Default constructor
     */
    public RandomMemberSelector() {
        this.createdMembers = new ArrayList<>();
        this.maxMembers = DEFAULT_MAX_MEMBERS;
    }

    /**
     * Test constructor
     *
     * @param maxMembers the max. nenbers allowed in the list
     */
    public RandomMemberSelector(int maxMembers) {
        this.createdMembers = new ArrayList<>();
        this.maxMembers = maxMembers;
    }

    /**
     * Selects a random member or creates a member
     *
     * @return a random or new member
     */
    public Member selectMember() {
        Member member = selectRandomMember();
        if (member != null) {
            return member;
        }
        return createNewMember();
    }

    /**
     * Selects a random member or null
     *
     * @return the existing member or null
     */
    private Member selectRandomMember() {
        if (createdMembers.isEmpty()) {
            return null;
        }

        int rand = ((new Random()).nextInt(maxMembers));
        if (rand >= createdMembers.size()) {
            return null;
        }

        Member sourceMember = createdMembers.get(rand);

        return Member.newBuilder(sourceMember).build();
    }

    /**
     * Creates a new member with random data
     *
     * @return the new member
     */
    private Member createNewMember() {
        Member.Builder mb = Member.newBuilder();
        mb.setUuid(UUID.randomUUID().toString());
        mb.setFistName(firstNames[(new Random()).nextInt(firstNames.length)]);
        mb.setLastName(lastNames[(new Random()).nextInt(lastNames.length)]);

        // Add 1 to 3 addresses
        int num = 1 + ((new Random()).nextInt(3));
        List<MemberAddress> memberAddresses = new ArrayList<>();
        while (num-- > 0) {
            int addr = (new Random()).nextInt(addresses.length);
            MemberAddress.Builder ab = MemberAddress.newBuilder();
            ab.setCountry(addresses[addr][0]);
            ab.setState(addresses[addr][1]);
            ab.setCity(addresses[addr][2]);
            ab.setZipCode(addresses[addr][3]);
            ab.setStreet(addresses[addr][4]);
            ab.setNumber(addresses[addr][5]);
            ab.setExtra(addresses[addr][6]);
            memberAddresses.add(ab.build());
        }
        mb.setAddresses(memberAddresses);

        Member member = mb.build();
        createdMembers.add(member);

        return member;
    }
}
