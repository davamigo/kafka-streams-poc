package com.example.kafka.streams.poc.kafka.producer;

import com.example.kafka.streams.poc.kafka.exception.KafkaProducerException;
import com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine;
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
 * Kafka producer to publish the warehouse order lines to the "recovered" topic data
 */
@Component
public class RecoveredWarehouseOrderLinesKafkaProducer {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(RecoveredWarehouseOrderLinesKafkaProducer.class);

    /** Kafka template for producing messages messages */
    private final KafkaTemplate<String, WarehouseOrderLine> warehouseOrderLineKafkaProducerTemplate;

    /** The name of the Kafka topic to publish messages */
    private final String recoveredWarehouseOrderLinesTopic;

    /**
     * Autowired constructor
     *
     * @param warehouseOrderLineKafkaProducerTemplate the Kafka template for producing messages
     */
    @Autowired
    public RecoveredWarehouseOrderLinesKafkaProducer(
            KafkaTemplate<String, WarehouseOrderLine> warehouseOrderLineKafkaProducerTemplate,
            @Value("${spring.kafka.topics.warehouse-order-lines-recovered}") String recoveredWarehouseOrderLinesTopic
    ) {
        this.warehouseOrderLineKafkaProducerTemplate = warehouseOrderLineKafkaProducerTemplate;
        this.recoveredWarehouseOrderLinesTopic = recoveredWarehouseOrderLinesTopic;
    }

    /**
     * Publish warehouse order line data to recovered topic
     *
     * @param warehouseOrderLine the warehouse order line
     * @throws KafkaProducerException when can't publish
     */
    public void publish(WarehouseOrderLine warehouseOrderLine) throws KafkaProducerException {
        publish(warehouseOrderLine, recoveredWarehouseOrderLinesTopic);
    }

    /**
     * Publish warehouse order line data to a topic
     *
     * @param warehouseOrderLine the warehouseOrderLine entity
     * @param topic              the name of the topic
     * @throws KafkaProducerException when can't publish
     */
    void publish(WarehouseOrderLine warehouseOrderLine, String topic) throws KafkaProducerException {
        LOGGER.info(">>> Publishing the warehouse order line {} to the topic {}...", warehouseOrderLine.getUuid(), topic);
        ProducerRecord<String, WarehouseOrderLine> record = new ProducerRecord<>(topic, warehouseOrderLine.getUuid(), warehouseOrderLine);
        try {
            SendResult<String, WarehouseOrderLine> result = warehouseOrderLineKafkaProducerTemplate.send(record).get();
            LOGGER.info(">>> The warehouse order line {} has been published to the topic {}!", warehouseOrderLine.getUuid(), result.getRecordMetadata().toString());
        } catch (InterruptedException | ExecutionException | KafkaException exc) {
            LOGGER.error(">>> An error occurred publishing the warehouse order line {} to the topic {}", warehouseOrderLine.getUuid(), topic);
            throw new KafkaProducerException(exc, warehouseOrderLine, topic);
        }
    }
}
