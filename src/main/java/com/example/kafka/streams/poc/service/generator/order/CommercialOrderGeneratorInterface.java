package com.example.kafka.streams.poc.service.generator.order;

import com.example.kafka.streams.poc.domain.entity.order.CommercialOrder;

/**
 * Interface to a service to get a commercial order for generating testing data
 */
public interface CommercialOrderGeneratorInterface {

    /**
     * Get a commercial order
     *
     * @return a commercial order
     */
    CommercialOrder getCommercialOrder();
}
