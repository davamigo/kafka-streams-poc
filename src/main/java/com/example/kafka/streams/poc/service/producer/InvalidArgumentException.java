package com.example.kafka.streams.poc.service.producer;

/**
 * Invalid argument in the use case exception
 */
public class InvalidArgumentException extends RuntimeException {

    /**
     * Constructs a new exception with the detail message.
     *
     * @param message the detail message.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
