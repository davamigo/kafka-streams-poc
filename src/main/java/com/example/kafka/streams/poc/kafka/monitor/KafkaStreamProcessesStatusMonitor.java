package com.example.kafka.streams.poc.kafka.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Component for monitoring Kafka Streams processes status beans
 */
@Component
public class KafkaStreamProcessesStatusMonitor {

    /** Map autowired with all the StreamsBuilderFactoryBean beans */
    private final Map<String, StreamsBuilderFactoryBean> beanMap;

    /**
     * Autowired constructor
     *
     * @param beanMap Map autowired with all the StreamsBuilderFactoryBean beans
     */
    @Autowired
    public KafkaStreamProcessesStatusMonitor(Map<String, StreamsBuilderFactoryBean> beanMap) {
        this.beanMap = beanMap;
    }

    /**
     * @return the qualifier of the beans (names)
     */
    public List<String> getBeanQualifiers() {
        return new ArrayList<>(beanMap.keySet());
    }

    /**
     * Method to get the bean by it's qualifier (name)
     *
     * @param qualifier the qualifier of the bean (name)
     * @return a bean in the map or null
     */
    public StreamsBuilderFactoryBean get(String qualifier) {
        return beanMap.get(qualifier);
    }

    /**
     * Safe method to get the bean by it's qualifier (name)
     *
     * @param qualifier the qualifier of the bean (name)
     * @return the bean
     * @throws BeanNotFoundException when bean not found
     */
    public StreamsBuilderFactoryBean safeGet(String qualifier) throws BeanNotFoundException {
        final StreamsBuilderFactoryBean bean = get(qualifier);
        if (null == bean) {
            throw new BeanNotFoundException(qualifier);
        }
        return bean;
    }

    /**
     * Checks whether a component is running by it's qualifier (name)
     *
     * @param qualifier the qualifier of the bean (name)
     * @return whether a component is currently running
     * @throws BeanNotFoundException when bean not found
     */
    public boolean isRunning(String qualifier) throws BeanNotFoundException {
        return safeGet(qualifier).isRunning();
    }

    /**
     * Starts a component by it's qualifier (name)
     *
     * @param qualifier the name of the bean
     * @throws BeanNotFoundException when bean not found
     */
    public void start(String qualifier) throws BeanNotFoundException {
        safeGet(qualifier).start();
    }

    /**
     * Starts all components
     */
    public void startAll() {
        StreamsBuilderFactoryBean bean;
        for (String qualifier: getBeanQualifiers()) {
            bean = get(qualifier);
            if (null != bean && !bean.isRunning()) {
                bean.start();
            }
        }
    }

    /**
     * Stops a component by it's qualifier (name)
     *
     * @param qualifier  the qualifier of the bean (name)
     * @throws BeanNotFoundException when bean not found
     */
    public void stop(String qualifier) throws BeanNotFoundException {
        safeGet(qualifier).stop();
    }

    /**
     * Stops all components
     */
    public void stopAll() {
        StreamsBuilderFactoryBean bean;
        for (String qualifier: getBeanQualifiers()) {
            bean = get(qualifier);
            if (null != bean && bean.isRunning()) {
                bean.stop();
            }
        }
    }
}
