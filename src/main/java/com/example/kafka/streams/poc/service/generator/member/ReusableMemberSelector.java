package com.example.kafka.streams.poc.service.generator.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service to reuse random generated members for generating testing data
 */
@Component
public class ReusableMemberSelector implements MemberGeneratorInterface {

    /** Service to generate the members with random data */
    private MemberGeneratorInterface memberGenerator;

    /** Default max. members in the list */
    private static final int DEFAULT_MAX_MEMBERS = 10;

    /** List of members created in this session */
    private List<Member> createdMembers;

    /** Max. members allowed in the list */
    private int maxMembers;

    /**
     * Autowired constructor
     *
     * @param memberGenerator the service to generate new members
     */
    @Autowired
    public ReusableMemberSelector(RandomMemberGenerator memberGenerator) {
        this.memberGenerator = memberGenerator;
        this.createdMembers = new ArrayList<>();
        this.maxMembers = DEFAULT_MAX_MEMBERS;
    }

    /**
     * Test constructor
     *
     * @param memberGenerator the service to generate new members
     * @param maxMembers the max. nenbers allowed in the list
     */
    public ReusableMemberSelector(RandomMemberGenerator memberGenerator, int maxMembers) {
        this.memberGenerator = memberGenerator;
        this.createdMembers = new ArrayList<>();
        this.maxMembers = maxMembers;
    }

    /**
     * Selects a random member from the list or creates a new member and adds it to the list
     *
     * @return a random or a new member
     */
    @Override
    public synchronized Member getMember() {

        // Select a random number from the stored members (or null)
        Member member = selectRandomMember();
        if (member != null) {
            return member;
        }

        // Generate a new member and store it in the list
        member = memberGenerator.getMember();
        createdMembers.add(member);

        // Return a copy of the member
        return Member.newBuilder().set(member).build();
    }

    /**
     * Selects a random member or null
     *
     * @return the existing member or null
     */
    private Member selectRandomMember() {

        // If the list is empty, return null
        if (createdMembers.isEmpty()) {
            return null;
        }

        // Generate a random index between 0 and max-members -1
        int index = ((new Random()).nextInt(maxMembers));

        // If the index is not in the list, return null
        if (index >= createdMembers.size()) {
            return null;
        }

        // Return a copy of the member at this position
        Member sourceMember = createdMembers.get(index);
        return Member.newBuilder().set(sourceMember).build();
    }
}
