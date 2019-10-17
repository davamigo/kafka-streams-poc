package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.warehouse.DefaultWarehouseOrderReceptionProcessor;
import com.example.kafka.streams.poc.service.processor.warehouse.WarehouseOrderReceptionProcessorInterface;
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
public class GeneratedWarehouseOrdersKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedWarehouseOrdersKafkaConsumer.class);

    /** Service to do process the reception of a generated warehouse order */
    private WarehouseOrderReceptionProcessorInterface warehouseOrderReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param warehouseOrderReceptionProcessor service to process the reception
     */
    @Autowired
    public GeneratedWarehouseOrdersKafkaConsumer(DefaultWarehouseOrderReceptionProcessor warehouseOrderReceptionProcessor) {
        this.warehouseOrderReceptionProcessor = warehouseOrderReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param order the data of the warehouse order
     * @param ack   the acknowledgment object
     * @param key   the key of the message in the topic
     * @param topic the name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.warehouse-orders-new}",
            groupId="${spring.kafka.group-ids.warehouse-orders-new}",
            containerFactory="warehouseOrderKafkaListenerContainerFactory"
    )
    public void listen(
            WarehouseOrder order,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming generated warehouse order: Topic={}, Key={}", topic, key);

        try {
            warehouseOrderReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder
                            .newBuilder()
                            .set(order)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The generated warehouse order with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received generated warehouse order: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
