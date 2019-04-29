package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.service.processor.ProcessorException;
import com.example.kafka.streams.poc.service.processor.order.NewCommercialOrderReceptionProcessorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka consumer to receive the new commercial orders data
 */
@Component
public class NewCommercialOrdersKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewCommercialOrdersKafkaConsumer.class);

    /** Service to do process the reception of a new CommercialOrder */
    private NewCommercialOrderReceptionProcessorInterface newCommercialOrderReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param newCommercialOrderReceptionProcessor service to process the reception
     */
    @Autowired
    public NewCommercialOrdersKafkaConsumer(NewCommercialOrderReceptionProcessorInterface newCommercialOrderReceptionProcessor) {
        this.newCommercialOrderReceptionProcessor = newCommercialOrderReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param order The data of the commercial order
     * @param ack   The acknowledgment object
     * @param key   The key of the commercial order
     * @param topic The topic name
     */
    @KafkaListener(
            topics="${spring.kafka.topics.commercial-orders-new}",
            groupId="${spring.kafka.group-ids.commercial-orders-new}",
            containerFactory="commercialOrderKafkaListenerContainerFactory"
    )
    public void listen(
            CommercialOrder order,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info("Received commercial order: Topic={}, Key={}", topic, key);

        try {
            newCommercialOrderReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.order.CommercialOrder
                            .newBuilder()
                            .set(order)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info("The commercial order with Key={} has been processed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error("An error occurred processing a received commercial order: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}