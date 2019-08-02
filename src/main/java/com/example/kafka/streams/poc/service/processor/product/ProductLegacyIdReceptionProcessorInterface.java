package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;

/**
 * Interface to a service to process the reception of a product legacy id
 */
public interface ProductLegacyIdReceptionProcessorInterface {

    /**
     * Process the reception of a product legacy id
     *
     * @param uuid the unique identifier of the product
     * @param legacyId the legacy id the product
     * @throws ProcessorException when an error occurred
     */
    void process(String uuid, int legacyId) throws ProcessorException;
}
