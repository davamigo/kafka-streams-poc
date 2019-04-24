package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.member.Member;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * Kafka producer to publish the members data
 */
@Component
public class NewMembersKafkaProducer {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewMembersKafkaProducer.class);

    /**
     * Kafka template for producing messages when new members created
     */
    private final KafkaTemplate<String, Member> memberKafkaProducerTemplate;

    /**
     * The name of the Kafka topic to read messages
     */
    private final String newMembersTopic;

    /**
     * Autowired constructor
     *
     * @param memberKafkaProducerTemplate the Kafka template for producing messages
     */
    @Autowired
    public NewMembersKafkaProducer(
            KafkaTemplate<String, Member> memberKafkaProducerTemplate,
            @Value("${spring.kafka.topics.members-new}") String newMembersTopic
    ) {
        this.memberKafkaProducerTemplate = memberKafkaProducerTemplate;
        this.newMembersTopic = newMembersTopic;
    }

    /**
     * Publish member data to new-member topic
     *
     * @param member the member entity
     * @throws KafkaProducerException when can't publish
     */
    public void publish(Member member) throws KafkaProducerException {
        publish(member, newMembersTopic);
    }

    /**
     * Publish member data to a topic
     *
     * @param member the member entity
     * @param topic   the name of the topic
     * @throws KafkaProducerException when can't publish
     */
    void publish(Member member, String topic) throws KafkaProducerException {
        LOGGER.info("Publishing the member {} to the topic {}...", member.getUuid(), topic);
        ProducerRecord<String, Member> record = new ProducerRecord<>(topic, member.getUuid(), member);
        try {
            SendResult<String, Member> result = memberKafkaProducerTemplate.send(record).get();
            LOGGER.info("The member {} has been published to the topic {}!", member.getUuid(), result.getRecordMetadata().toString());
        } catch (InterruptedException | ExecutionException | KafkaException exc) {
            LOGGER.error("An error occurred publishing the member {} to the topic {}", member.getUuid(), topic);
            throw new KafkaProducerException(exc, member, topic);
        }
    }
}
