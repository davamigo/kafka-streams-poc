package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a split commercial order line
 */
public interface SplitCommercialOrderLineReceptionProcessorInterface {

    /**
     * Process the reception of a split commercial order line
     *
     * @param line the split commercial order line received
     * @throws ProcessorException when an error occurred
     */
    void process(CommercialOrderLineSplit line) throws ProcessorException;
}
