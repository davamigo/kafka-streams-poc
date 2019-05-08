package com.example.kafka.streams.poc.service.processor.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a new Member
 */
public interface NewMemberReceptionProcessorInterface {

    /**
     * Process the reception of a member
     *
     * @param member the member received
     * @throws ProcessorException when an error occurred
     */
    void process(Member member) throws ProcessorException;
}
