package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.service.processor.member.NewMemberReceptionProcessorInterface;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer to receive the new members data
 */
@Component
public class NewMembersKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewMembersKafkaConsumer.class);

    /** Service to do process the reception of a new Member */
    private NewMemberReceptionProcessorInterface newMemberReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param newMemberReceptionProcessor service to process the reception
     */
    @Autowired
    public NewMembersKafkaConsumer(NewMemberReceptionProcessorInterface newMemberReceptionProcessor) {
        this.newMemberReceptionProcessor = newMemberReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param member The data of the member
     * @param ack    The acknowledgment object
     * @param key    The key of the message in the topic
     * @param topic  The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.members-new}",
            groupId="${spring.kafka.group-ids.members-new}",
            containerFactory="memberKafkaListenerContainerFactory"
    )
    public void listen(
            Member member,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming member: Topic={}, Key={}", topic, key);

        try {
            newMemberReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.member.Member
                            .newBuilder()
                            .set(member)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The member with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received member: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
