package com.example.kafka.streams.poc.service.processor.exception;

/**
 * Exception to be thrown when an error occurred with the processor
 */
public class ProcessorException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public ProcessorException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
