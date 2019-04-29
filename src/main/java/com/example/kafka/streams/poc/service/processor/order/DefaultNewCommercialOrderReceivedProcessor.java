package com.example.kafka.streams.poc.service.processor.order;

import com.example.kafka.streams.poc.domain.entity.order.CommercialOrder;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderRepository;
import com.example.kafka.streams.poc.service.processor.ProcessorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to process the reception of a new CommercialOrder whichstores the commercial order in a mongoDB collection.
 */
@Component
public class DefaultNewCommercialOrderReceivedProcessor implements NewCommercialOrderReceptionProcessorInterface {

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
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred inserting a commercial order in the mongo DB database", exc);
        }
    }
}
