package com.example.kafka.streams.poc.service.processor.member;

import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.mongodb.entity.MemberEntity;
import com.example.kafka.streams.poc.mongodb.repository.MemberRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a new Member which stores the member in a mongoDB collection.
 */
@Component
public class DefaultNewMemberReceivedProcessor implements NewMemberReceptionProcessorInterface{

    /** The mongoDB repository where to store the members received */
    private MemberRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB cmember repository
     */
    @Autowired
    public DefaultNewMemberReceivedProcessor(MemberRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a member
     *
     * @param member the member received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(Member member) throws ProcessorException {
        try {
            Optional<MemberEntity> queryResult = repository.findById(member.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new MemberEntity(member));
            }
            else {
                repository.insert(new MemberEntity(member));
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred inserting a member in the mongo DB database", exc);
        }
    }
}
