package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.product.Product;
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
 * Kafka producer to publish the new products data
 */
@Component
public class NewProductsKafkaProducer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewProductsKafkaProducer.class);

    /** Kafka template for producing messages when new products created */
    private final KafkaTemplate<String, Product> productKafkaProducerTemplate;

    /** The name of the Kafka topic to publish the messages */
    private final String newProductsTopic;

    /**
     * Autowired constructor
     *
     * @param productKafkaProducerTemplate the Kafka template for producing messages
     */
    @Autowired
    public NewProductsKafkaProducer(
            KafkaTemplate<String, Product> productKafkaProducerTemplate,
            @Value("${spring.kafka.topics.products-new}") String newProductsTopic
    ) {
        this.productKafkaProducerTemplate = productKafkaProducerTemplate;
        this.newProductsTopic = newProductsTopic;
    }

    /**
     * Publish product data to new-product topic
     *
     * @param product the product entity
     * @throws KafkaProducerException when can't publish
     */
    public void publish(Product product) throws KafkaProducerException {
        publish(product, newProductsTopic);
    }

    /**
     * Publish product data to a topic
     *
     * @param product the product entity
     * @param topic   the name of the topic
     * @throws KafkaProducerException when can't publish
     */
    void publish(Product product, String topic) throws KafkaProducerException {
        LOGGER.info(">>> Publishing the product {} to the topic {}...", product.getUuid(), topic);
        ProducerRecord<String, Product> record = new ProducerRecord<>(topic, product.getUuid(), product);
        try {
            SendResult<String, Product> result = productKafkaProducerTemplate.send(record).get();
            LOGGER.info(">>> The product {} has been published to the topic {}!", product.getUuid(), result.getRecordMetadata().toString());
        } catch (InterruptedException | ExecutionException | KafkaException exc) {
            LOGGER.error(">>> An error occurred publishing the product {} to the topic {}", product.getUuid(), topic);
            throw new KafkaProducerException(exc, product, topic);
        }
    }
}
