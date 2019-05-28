package com.example.kafka.streams.poc.service.api;

import java.util.Optional;

/**
 * Interface to a service to get the legacy Id of a product from the Uuid.
 */
public interface LegacyProductIdsApiInterface {

    /**
     * Get the legacy id. of a product from the uuid.
     *
     * @param productUuid the product unique identifier
     * @return the legacy id of the product
     */
    Optional<Integer> getLegacyId(String productUuid);
}
