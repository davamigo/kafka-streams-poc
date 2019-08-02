package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineUnmatchedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineUnmatchedRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a unmatched warehouse order line which stores it in a mongoDB collection.
 */
@Component
public class DefaultUnmatchedWarehouseOrderLineReceptionProcessor implements WarehouseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUnmatchedWarehouseOrderLineReceptionProcessor.class);

    /** The mongoDB repository where to store the unmatched warehouse order lines received */
    private WarehouseOrderLineUnmatchedRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB unmatched warehouse order line repository
     */
    @Autowired
    public DefaultUnmatchedWarehouseOrderLineReceptionProcessor(WarehouseOrderLineUnmatchedRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a unmatched warehouse order line
     *
     * @param line the unmatched warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrderLine line) throws ProcessorException {

        try {
            Optional<WarehouseOrderLineUnmatchedEntity> queryResult = repository.findById(line.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderLineUnmatchedEntity(line));
                LOGGER.info(">>> Warehouse order line unmatched key={} updated in mongoDB", line.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderLineUnmatchedEntity(line));
                LOGGER.info(">>> Warehouse order line unmatched key={} inserted in mongoDB", line.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order unmatched in the mongoDB database", exc);
        }
    }
}
