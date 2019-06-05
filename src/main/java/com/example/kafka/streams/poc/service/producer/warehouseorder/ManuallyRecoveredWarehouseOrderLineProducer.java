package com.example.kafka.streams.poc.service.producer.warehouseorder;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.kafka.producer.RecoveredWarehouseOrderLinesKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to publish manually recovered warehouse order lines to the recovered topic
 */
@Component
public class ManuallyRecoveredWarehouseOrderLineProducer {

    /** Kafka producer to publish the recovered warehouse orders lines */
    private RecoveredWarehouseOrderLinesKafkaProducer recoveredWarehouseOrderLinesKafkaProducer;

    /**
     * Autowired constructor
     *
     * @param recoveredWarehouseOrderLinesKafkaProducer Kafka producer to publish the new commercial orders data
     */
    @Autowired
    public ManuallyRecoveredWarehouseOrderLineProducer(RecoveredWarehouseOrderLinesKafkaProducer recoveredWarehouseOrderLinesKafkaProducer) {
        this.recoveredWarehouseOrderLinesKafkaProducer = recoveredWarehouseOrderLinesKafkaProducer;
    }

    /**
     * Publishes the commercial order data into a Kafka topic
     *
     * @param warehouseOrderLine the commercial order data
     */
    public void publish(WarehouseOrderLine warehouseOrderLine) {
        recoveredWarehouseOrderLinesKafkaProducer.publish(WarehouseOrderLine.newAvroExporter(warehouseOrderLine).export());
    }
}
