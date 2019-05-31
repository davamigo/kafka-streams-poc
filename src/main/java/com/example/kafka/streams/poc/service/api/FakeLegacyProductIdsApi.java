package com.example.kafka.streams.poc.service.api;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * This is a fake class for implementing LegacyProductIdsApiInterface
 *
 * It returns a random integer Id or an empty result in some cases after sleeping the thread to simulate an API call.
 */
@Component
public class FakeLegacyProductIdsApi implements LegacyProductIdsApiInterface {

    /** Map to store the previous values (to return always the same value) */
    private Map<String, Optional<Integer>> previousValues;

    /** The wait time per call in milliseconds */
    private final int watTimeMillis;

    /** The failure rate */
    private final int failRate;

    /** Constants for the default values of wait time and fail ratee */
    private final int DEFAULT_WAIT_TIME_MILLIS = 250;
    private final int DEFAULT_FAIL_RATE = 10;

    /**
     * Default constructor
     */
    public FakeLegacyProductIdsApi() {
        this.previousValues = new HashMap<>();
        this.watTimeMillis = DEFAULT_WAIT_TIME_MILLIS;
        this.failRate = DEFAULT_FAIL_RATE;
    }

    /**
     * Test constructor
     *
     * @param watTimeMillis the wait time per call in milliseconds
     */
    public FakeLegacyProductIdsApi(int watTimeMillis, int failRate) {
        this.previousValues = new HashMap<>();
        this.watTimeMillis = watTimeMillis;
        this.failRate = failRate;
    }

    /**
     * Get the legacy id. of a product from the uuid.
     *
     * @param productUuid the product unique identifier
     * @return the legacy id of the product
     */
    @Override
    public synchronized Optional<Integer> getLegacyId(String productUuid) {

        // Get from previous values if exist
        if (previousValues.containsKey(productUuid)) {
            return previousValues.get(productUuid);
        }

        // Sleep a fixed number of milliseconds to simulate the call to the API
        try {
            Thread.sleep(watTimeMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Optional<Integer> result;

        // Introduce random failures depending the rate
        int percent = 100 - (new Random()).nextInt(100);
        if (failRate > percent) {
            result = Optional.empty();
        }
        else {
            // Get random legacy id between 100000 and 999999
            int legacyId = 100000 + (new Random()).nextInt(899999);
            result = Optional.of(legacyId);
        }

        // Store previous values
        previousValues.put(productUuid, result);

        return result;
    }
}
