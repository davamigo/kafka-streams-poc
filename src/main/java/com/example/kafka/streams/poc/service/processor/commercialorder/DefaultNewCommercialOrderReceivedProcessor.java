package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to process the reception of a new CommercialOrder which stores the commercial order in a mongoDB collection.
 */
@Component
public class DefaultNewCommercialOrderReceivedProcessor implements NewCommercialOrderReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNewCommercialOrderReceivedProcessor.class);

    /** The mongoDB repository where to store the commercial orders received */
    private CommercialOrderRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB commercial order repository
     */
    @Autowired
    public DefaultNewCommercialOrderReceivedProcessor(CommercialOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a commercial order
     *
     * @param commercialOrder the commercial order received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(CommercialOrder commercialOrder) throws ProcessorException {
        try {
            repository.insert(new CommercialOrderEntity(commercialOrder));
            LOGGER.info(">>> Commercial order uuid={} inserted in mongoDB", commercialOrder.getUuid());
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a commercial order in the mongoDB database", exc);
        }
    }
}
