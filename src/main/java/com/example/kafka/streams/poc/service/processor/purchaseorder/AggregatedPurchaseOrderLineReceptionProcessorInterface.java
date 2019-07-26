package com.example.kafka.streams.poc.service.processor.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a generated purchase order line
 */
public interface AggregatedPurchaseOrderLineReceptionProcessorInterface {

    /**
     * Process the reception of a generated purchase order line
     *
     * @param purchaseOrderLine the purchase order line received
     * @throws ProcessorException when an error occurred
     */
    void process(PurchaseOrderLine purchaseOrderLine) throws ProcessorException;
}
