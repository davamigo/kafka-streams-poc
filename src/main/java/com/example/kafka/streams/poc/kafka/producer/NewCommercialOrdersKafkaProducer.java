package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
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
 * Kafka producer to publish the new commercial orders data
 */
@Component
public class NewCommercialOrdersKafkaProducer {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewCommercialOrdersKafkaProducer.class);

    /**
     * Kafka template for producing messages when new commercial orders created
     */
    private final KafkaTemplate<String, CommercialOrder> commercialOrderKafkaProducerTemplate;

    /**
     * The name of the Kafka topic to read messages
     */
    private final String newCommercialOrdersTopic;

    /**
     * Autowired constructor
     *
     * @param commercialOrderKafkaProducerTemplate the Kafka template for producing messages
     */
    @Autowired
    public NewCommercialOrdersKafkaProducer(
            KafkaTemplate<String, CommercialOrder> commercialOrderKafkaProducerTemplate,
            @Value("${spring.kafka.topics.commercial-orders-new}") String newCommercialOrdersTopic
    ) {
        this.commercialOrderKafkaProducerTemplate = commercialOrderKafkaProducerTemplate;
        this.newCommercialOrdersTopic = newCommercialOrdersTopic;
    }

    /**
     * Publish commercial order data to new-commercial-order topic
     *
     * @param commercialOrder the commercialOrder entity
     * @throws KafkaProducerException when can't publish
     */
    public void publish(CommercialOrder commercialOrder) throws KafkaProducerException {
        publish(commercialOrder, newCommercialOrdersTopic);
    }

    /**
     * Publish commercial order data to a topic
     *
     * @param commercialOrder the commercia oOrder entity
     * @param topic           the name of the topic
     * @throws KafkaProducerException when can't publish
     */
    void publish(CommercialOrder commercialOrder, String topic) throws KafkaProducerException {
        LOGGER.info("Publishing the commercialOrder {} to the topic {}...", commercialOrder.getUuid(), topic);
        ProducerRecord<String, CommercialOrder> record = new ProducerRecord<>(topic, commercialOrder.getUuid(), commercialOrder);
        try {
            SendResult<String, CommercialOrder> result = commercialOrderKafkaProducerTemplate.send(record).get();
            LOGGER.info("The commercial order {} has been published to the topic {}!", commercialOrder.getUuid(), result.getRecordMetadata().toString());
        } catch (InterruptedException | ExecutionException | KafkaException exc) {
            LOGGER.error("An error occurred publishing the commercial order {} to the topic {}", commercialOrder.getUuid(), topic);
            throw new KafkaProducerException(exc, commercialOrder, topic);
        }
    }
}
