package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a warehouse order
 */
public interface WarehouseOrderReceptionProcessorInterface {

    /**
     * Process the reception of a warehouse order
     *
     * @param warehouseOrder the warehouse order received
     * @throws ProcessorException when an error occurred
     */
    void process(WarehouseOrder warehouseOrder) throws ProcessorException;
}
