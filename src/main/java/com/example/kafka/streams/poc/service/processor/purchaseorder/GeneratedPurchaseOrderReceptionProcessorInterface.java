package com.example.kafka.streams.poc.service.processor.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a generated Purchase order
 */
public interface GeneratedPurchaseOrderReceptionProcessorInterface {

    /**
     * Process the reception of a generated purchase order
     *
     * @param purchaseOrder the purchase order received
     * @throws ProcessorException when an error occurred
     */
    void process(PurchaseOrder purchaseOrder) throws ProcessorException;
}
