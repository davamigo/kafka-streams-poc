package com.example.kafka.streams.poc.kafka.monitor;

/**
 * Exception to be thrown by KafkaStreamProcessesStatusMonitor when a bean was not found
 */
public class BeanNotFoundException extends Exception {

    /**
     * Default constructor
     *
     * @param qualifier the bean's name
     */
    public BeanNotFoundException(String qualifier) {
        super("KafkaStreamProcessesStatusMonitor: The bean " + qualifier + " was not found!");
    }
}
