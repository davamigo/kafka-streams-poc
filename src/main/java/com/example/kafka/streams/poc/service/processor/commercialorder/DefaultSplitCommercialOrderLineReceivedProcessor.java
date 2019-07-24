package com.example.kafka.streams.poc.service.processor.commercialorder;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderLineSplitEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderLineSplitRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to process the reception of a split commercial order line which stores the order in a mongoDB collection.
 */
@Component
public class DefaultSplitCommercialOrderLineReceivedProcessor implements SplitCommercialOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSplitCommercialOrderLineReceivedProcessor.class);

    /** The mongoDB repository where to store the split commercial order lines received */
    private CommercialOrderLineSplitRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB split commercial order line repository
     */
    @Autowired
    public DefaultSplitCommercialOrderLineReceivedProcessor(CommercialOrderLineSplitRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a split commercial order line
     *
     * @param line the split commercial order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(CommercialOrderLineSplit line) throws ProcessorException {
        try {
            repository.insert(new CommercialOrderLineSplitEntity(line));
            LOGGER.info(">>> Split commercial order line uuid={} inserted in mongoDB", line.getUuid());
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a split commercial order line in the mongoDB database", exc);
        }
    }
}
