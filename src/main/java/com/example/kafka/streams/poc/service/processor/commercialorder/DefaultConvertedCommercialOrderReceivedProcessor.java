package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderConvertedEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderConvertedRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to process the reception of a converted commercial order which stores the order in a mongoDB collection.
 */
@Component
public class DefaultConvertedCommercialOrderReceivedProcessor implements ConvertedCommercialOrderReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConvertedCommercialOrderReceivedProcessor.class);

    /** The mongoDB repository where to store the converted commercial orders received */
    private CommercialOrderConvertedRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB converted commercial order repository
     */
    @Autowired
    public DefaultConvertedCommercialOrderReceivedProcessor(CommercialOrderConvertedRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a converted commercial order
     *
     * @param order the converted commercial order received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(CommercialOrderConverted order) throws ProcessorException {
        try {
            repository.insert(new CommercialOrderConvertedEntity(order));
            LOGGER.info(">>> Commercial order converted uuid={} inserted in mongoDB", order.getUuid());
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a converted commercial order in the mongoDB database", exc);
        }
    }
}
