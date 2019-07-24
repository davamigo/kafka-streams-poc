package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted;
import com.example.kafka.streams.poc.service.processor.commercialorder.ConvertedCommercialOrderReceptionProcessorInterface;
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
 * Kafka consumer to receive the converted commercial orders data
 */
@Component
public class ConvertedCommercialOrdersKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertedCommercialOrdersKafkaConsumer.class);

    /** Service to do process the reception of a converted commercial order */
    private ConvertedCommercialOrderReceptionProcessorInterface convertedCommercialOrderReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param convertedCommercialOrderReceptionProcessor service to process the reception
     */
    @Autowired
    public ConvertedCommercialOrdersKafkaConsumer(ConvertedCommercialOrderReceptionProcessorInterface convertedCommercialOrderReceptionProcessor) {
        this.convertedCommercialOrderReceptionProcessor = convertedCommercialOrderReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param order The data of the converted commercial order
     * @param ack   The acknowledgment object
     * @param key   The key of the message in the topic
     * @param topic The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.commercial-orders-converted}",
            groupId="${spring.kafka.group-ids.commercial-orders-converted}",
            containerFactory="commercialOrderConvertedKafkaListenerContainerFactory"
    )
    public void listen(
            CommercialOrderConverted order,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming converted commercial order: Topic={}, Key={}", topic, key);

        try {
            convertedCommercialOrderReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted
                            .newBuilder()
                            .set(order)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The converted commercial order with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received converted commercial order: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
