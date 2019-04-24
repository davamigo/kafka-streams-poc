package com.example.kafka.streams.poc.kafka.exception;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.product.Product;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;

/**
 * Exception to be thrown when an error occurred publishing in a Kafka topic
 */
public class KafkaProducerException extends KafkaException {

    /** The record data who caused the exception */
    private SpecificRecordBase recordData;

    /** The name of the topic */
    private String topic;

    /**
     * Constructor
     *
     * @param cause   the nested exception
     * @param product the product data
     * @param topic   the topic name
     */
    public KafkaProducerException(Throwable cause, Product product, String topic) {
        super(KafkaProducerException.buildErrorMessage("product", product.getUuid(), topic), cause);
        this.recordData = product;
        this.topic = topic;
    }

    /**
     * Constructor
     *
     * @param cause   the nested exception
     * @param member  the member data
     * @param topic   the topic name
     */
    public KafkaProducerException(Throwable cause, Member member, String topic) {
        super(KafkaProducerException.buildErrorMessage("member", member.getUuid(), topic), cause);
        this.recordData = member;
        this.topic = topic;
    }

    /**
     * Constructor
     *
     * @param cause            the nested exception
     * @param commercialOrder  the commercialOrder data
     * @param topic            the topic name
     */
    public KafkaProducerException(Throwable cause, CommercialOrder commercialOrder, String topic) {
        super(KafkaProducerException.buildErrorMessage("commercial order", commercialOrder.getUuid(), topic), cause);
        this.recordData = commercialOrder;
        this.topic = topic;
    }

    /**
     * @return the record data
     */
    public SpecificRecordBase getRecordData() {
        return recordData;
    }

    /**
     * @return the name of the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param entityName the name of the entity
     * @param uuid       the uuid of the entity
     * @param topic      the topic name
     * @return the error message
     */
    private static String buildErrorMessage(String entityName, String uuid, String topic) {
        return "An error occurred publishing the " + entityName + " " + uuid + "to the Kafka topic " + topic + "!";
    }
}
