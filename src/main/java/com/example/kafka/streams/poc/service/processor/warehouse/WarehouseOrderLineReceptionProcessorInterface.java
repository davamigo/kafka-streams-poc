package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a warehouse order line (new, matched, unmatched, recovered, failed, full)
 */
public interface WarehouseOrderLineReceptionProcessorInterface {

    /**
     * Process the reception of a warehouse order line
     *
     * @param warehouseOrderLine the warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    void process(WarehouseOrderLine warehouseOrderLine) throws ProcessorException;
}
