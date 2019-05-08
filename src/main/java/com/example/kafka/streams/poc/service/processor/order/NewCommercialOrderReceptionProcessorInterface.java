package com.example.kafka.streams.poc.service.processor.order;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.service.processor.ProcessorException;

/**
 * Interface to a service to process the reception of a new CommercialOrder
 */
public interface NewCommercialOrderReceptionProcessorInterface {

    /**
     * Process the reception of a commercial order
     *
     * @param commercialOrder the commercial order received
     * @throws ProcessorException when an error occurred
     */
    void process(CommercialOrder commercialOrder) throws ProcessorException;
}
