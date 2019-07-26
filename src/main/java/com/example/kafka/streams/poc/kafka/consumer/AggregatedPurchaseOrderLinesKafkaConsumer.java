package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.purchaseorder.AggregatedPurchaseOrderLineReceptionProcessorInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer to receive the new purchase order lines data
 */
@Component
public class AggregatedPurchaseOrderLinesKafkaConsumer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatedPurchaseOrderLinesKafkaConsumer.class);

    /** Service to do process the reception of a new PurchaseOrderLine */
    private AggregatedPurchaseOrderLineReceptionProcessorInterface aggregatedPurchaseOrderLineReceptionProcessor;

    /**
     * Autowired constructor
     *
     * @param aggregatedPurchaseOrderLineReceptionProcessor service to process the reception
     */
    @Autowired
    public AggregatedPurchaseOrderLinesKafkaConsumer(AggregatedPurchaseOrderLineReceptionProcessorInterface aggregatedPurchaseOrderLineReceptionProcessor) {
        this.aggregatedPurchaseOrderLineReceptionProcessor = aggregatedPurchaseOrderLineReceptionProcessor;
    }

    /**
     * Kafka listener
     *
     * @param purchaseOrderLine The data of the purchase order line
     * @param ack               The acknowledgment object
     * @param key               The key of the message in the topic
     * @param topic             The name of the topic
     */
    @KafkaListener(
            topics="${spring.kafka.topics.purchase-order-lines-aggregated}",
            groupId="${spring.kafka.group-ids.purchase-order-lines-aggregated}",
            containerFactory="purchaseOrderLineKafkaListenerContainerFactory"
    )
    public void listen(
            PurchaseOrderLine purchaseOrderLine,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        LOGGER.info(">>> Consuming purchase order: Topic={}, Key={}", topic, key);

        try {
            aggregatedPurchaseOrderLineReceptionProcessor.process(
                    com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine
                            .newBuilder()
                            .set(purchaseOrderLine)
                            .build()
            );
            ack.acknowledge();

            LOGGER.info(">>> The purchase order line with Key={} has been consumed!", key);
        }
        catch (ProcessorException exc) {
            LOGGER.error(">>> An error occurred consuming a received purchase order line: Key={}, Message={}", topic, exc.getMessage());
            exc.printStackTrace();
        }
    }
}
