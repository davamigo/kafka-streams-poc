package com.example.kafka.streams.poc.kafka.exception;

import com.example.kafka.streams.poc.schemas.product.Product;
import org.springframework.kafka.KafkaException;

/**
 * Exception to be thrown when an error occurred publishing in a Kafka topic
 */
public class KafkaProducerException extends KafkaException {

    /** The product data who caused the exception */
    private Product product;

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
        super(KafkaProducerException.buildErrorMessage(product, topic), cause);
        this.product = product;
        this.topic = topic;
    }

    /**
     * @return the product data
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @return the name of the topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param product the product data
     * @param topic   the topic name
     * @return the error message
     */
    private static String buildErrorMessage(Product product, String topic) {
        return "An error occurred publishing the product " + product.getUuid() + "to the Kafka topic " + topic + "!";
    }
}
