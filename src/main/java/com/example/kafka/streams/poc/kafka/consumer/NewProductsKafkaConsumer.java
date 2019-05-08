package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.product.Product;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.product.NewProductReceptionProcessorInterface;
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
public class NewProductsKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewProductsKafkaConsumer.class);

    /** Service to do process the reception of a new Product */
    private NewProductReceptionProcessorInterface newProductReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param newProductReceptionProcessor service to process the reception
     */
    @Autowired
    public NewProductsKafkaConsumer(NewProductReceptionProcessorInterface newProductReceptionProcessor) {
        this.newProductReceptionProcessor = newProductReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param product The data of the product
     * @param ack    The acknowledgment object
     * @param key    The key of the message in the topic
     * @param topic  The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.products-new}",
            groupId="${spring.kafka.group-ids.products-new}",
            containerFactory="productKafkaListenerContainerFactory"
    )
    public void listen(
            Product product,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming product: Topic={}, Key={}", topic, key);

        try {
            newProductReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.product.Product
                            .newBuilder()
                            .set(product)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The product with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received product: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
