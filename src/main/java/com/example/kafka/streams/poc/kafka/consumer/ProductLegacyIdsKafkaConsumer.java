package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.product.NewProductReceptionProcessorInterface;
import com.example.kafka.streams.poc.service.processor.product.ProductLegacyIdReceptionProcessorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer to receive the new products data
 */
@Component
public class ProductLegacyIdsKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLegacyIdsKafkaConsumer.class);

    /** Service to do process the reception of a product legacy id */
    private ProductLegacyIdReceptionProcessorInterface processor;

    /**
     * Autowired constructor
     *
     * @param processor service to process the reception
     */
    @Autowired
    public ProductLegacyIdsKafkaConsumer(ProductLegacyIdReceptionProcessorInterface processor) {
        this.processor = processor;
    }

    /**
     * Kafka listener
     *
     * @param legacyId The legacy id. of the product
     * @param ack      The acknowledgment object
     * @param key      The key of the message in the topic
     * @param topic    The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.product-legacy-ids}",
            groupId="${spring.kafka.group-ids.product-legacy-ids}",
            containerFactory="productLegacyIdKafkaListenerContainerFactory"
    )
    public void listen(
            Integer legacyId,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming product legacy id: Topic={}, Key={}, legacyId={}", topic, key, legacyId);

        try {
            processor.process(key, legacyId);
            ack.acknowledge();

            LOGGER.info(">>> The product legacy id. with uuid={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received product legacy id.: uuid={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
