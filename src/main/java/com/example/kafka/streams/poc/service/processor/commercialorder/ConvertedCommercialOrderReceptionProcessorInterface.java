package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a converted commercial order
 */
public interface ConvertedCommercialOrderReceptionProcessorInterface {

    /**
     * Process the reception of a converted commercial order
     *
     * @param order the converted commercial order received
     * @throws ProcessorException when an error occurred
     */
    void process(CommercialOrderConverted order) throws ProcessorException;
}
