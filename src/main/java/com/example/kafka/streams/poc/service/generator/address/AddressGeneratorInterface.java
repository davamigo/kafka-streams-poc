package com.example.kafka.streams.poc.service.generator.address;

import com.example.kafka.streams.poc.domain.entity.address.Address;

/**
 * Interface to a service to get an address for generating testing data
 */
public interface AddressGeneratorInterface {

    /**
     * Get an address
     *
     * @return an address
     */
    Address getAddress();
}
