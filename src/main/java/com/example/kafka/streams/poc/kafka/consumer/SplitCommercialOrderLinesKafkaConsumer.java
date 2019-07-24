package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.service.processor.commercialorder.SplitCommercialOrderLineReceptionProcessorInterface;
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
 * Kafka consumer to receive the split commercial order lines data
 */
@Component
public class SplitCommercialOrderLinesKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SplitCommercialOrderLinesKafkaConsumer.class);

    /** Service to do process the reception of a split commercial order line */
    private SplitCommercialOrderLineReceptionProcessorInterface splitCommercialOrderLineReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param splitCommercialOrderLineReceptionProcessor service to process the reception
     */
    @Autowired
    public SplitCommercialOrderLinesKafkaConsumer(SplitCommercialOrderLineReceptionProcessorInterface splitCommercialOrderLineReceptionProcessor) {
        this.splitCommercialOrderLineReceptionProcessor = splitCommercialOrderLineReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param line  The data of the split commercial order line
     * @param ack   The acknowledgment object
     * @param key   The key of the message in the topic
     * @param topic The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.commercial-order-lines-split}",
            groupId="${spring.kafka.group-ids.commercial-order-lines-split}",
            containerFactory="commercialOrderLineSplitKafkaListenerContainerFactory"
    )
    public void listen(
            CommercialOrderLineSplit line,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming split commercial order line: Topic={}, Key={}", topic, key);

        try {
            splitCommercialOrderLineReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit
                            .newBuilder()
                            .set(line)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The split commercial order line with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received split commercial order line: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
