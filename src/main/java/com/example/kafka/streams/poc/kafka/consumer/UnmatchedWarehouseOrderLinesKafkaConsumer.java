package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.warehouse.DefaultUnmatchedWarehouseOrderLineReceptionProcessor;
import com.example.kafka.streams.poc.service.processor.warehouse.WarehouseOrderLineReceptionProcessorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer to receive the new purchase orders data
 */
@Component
public class UnmatchedWarehouseOrderLinesKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnmatchedWarehouseOrderLinesKafkaConsumer.class);

    /** Service to do process the reception of a unmatched warehouse order line */
    private WarehouseOrderLineReceptionProcessorInterface unmatchedWarehouseOrderLineReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param unmatchedWarehouseOrderLineReceptionProcessor service to process the reception
     */
    @Autowired
    public UnmatchedWarehouseOrderLinesKafkaConsumer(DefaultUnmatchedWarehouseOrderLineReceptionProcessor unmatchedWarehouseOrderLineReceptionProcessor) {
        this.unmatchedWarehouseOrderLineReceptionProcessor = unmatchedWarehouseOrderLineReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param line  the data of the warehouse order line
     * @param ack   the acknowledgment object
     * @param key   the key of the message in the topic
     * @param topic the name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.warehouse-order-lines-unmatched}",
            groupId="${spring.kafka.group-ids.warehouse-order-lines-unmatched}",
            containerFactory="warehouseOrderLineKafkaListenerContainerFactory"
    )
    public void listen(
            WarehouseOrderLine line,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming unmatched warehouse order line: Topic={}, Key={}", topic, key);

        try {
            unmatchedWarehouseOrderLineReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine
                            .newBuilder()
                            .set(line)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The unmatched warehouse order line with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received unmatched warehouse order line: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
