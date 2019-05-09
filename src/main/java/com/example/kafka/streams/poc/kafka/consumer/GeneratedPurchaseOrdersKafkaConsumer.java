package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder;
import com.example.kafka.streams.poc.service.processor.purchaseorder.GeneratedPurchaseOrderReceptionProcessorInterface;
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
 * Kafka consumer to receive the new purchase orders data
 */
@Component
public class GeneratedPurchaseOrdersKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedPurchaseOrdersKafkaConsumer.class);

    /** Service to do process the reception of a new PurchaseOrder */
    private GeneratedPurchaseOrderReceptionProcessorInterface generatedPurchaseOrderReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param generatedPurchaseOrderReceptionProcessor service to process the reception
     */
    @Autowired
    public GeneratedPurchaseOrdersKafkaConsumer(GeneratedPurchaseOrderReceptionProcessorInterface generatedPurchaseOrderReceptionProcessor) {
        this.generatedPurchaseOrderReceptionProcessor = generatedPurchaseOrderReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param purchaseOrder The data of the purchase order
     * @param ack           The acknowledgment object
     * @param key           The key of the message in the topic
     * @param topic         The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.purchase-orders-generated}",
            groupId="${spring.kafka.group-ids.purchase-orders-generated}",
            containerFactory="purchaseOrderKafkaListenerContainerFactory"
    )
    public void listen(
            PurchaseOrder purchaseOrder,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming purchase order: Topic={}, Key={}", topic, key);

        try {
            generatedPurchaseOrderReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder
                            .newBuilder()
                            .set(purchaseOrder)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The purchase order with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received purchase order: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
