package com.example.kafka.streams.poc.service.generator.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;

/**
 * Interface to a service to get a member for generating testing data
 */
public interface MemberGeneratorInterface {

    /**
     * Get a member
     *
     * @return a member
     */
    Member getMember();
}
