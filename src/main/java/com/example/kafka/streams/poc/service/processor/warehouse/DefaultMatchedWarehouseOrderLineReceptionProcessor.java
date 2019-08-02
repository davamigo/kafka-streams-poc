package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineMatchedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineMatchedRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a matched warehouse order line which stores it in a mongoDB collection.
 */
@Component
public class DefaultMatchedWarehouseOrderLineReceptionProcessor implements WarehouseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMatchedWarehouseOrderLineReceptionProcessor.class);

    /** The mongoDB repository where to store the matched warehouse order lines received */
    private WarehouseOrderLineMatchedRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB matched warehouse order line repository
     */
    @Autowired
    public DefaultMatchedWarehouseOrderLineReceptionProcessor(WarehouseOrderLineMatchedRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a matched warehouse order line
     *
     * @param line the matched warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrderLine line) throws ProcessorException {

        try {
            Optional<WarehouseOrderLineMatchedEntity> queryResult = repository.findById(line.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderLineMatchedEntity(line));
                LOGGER.info(">>> Warehouse order line matched key={} updated in mongoDB", line.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderLineMatchedEntity(line));
                LOGGER.info(">>> Warehouse order line matched key={} inserted in mongoDB", line.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order matched in the mongoDB database", exc);
        }
    }
}
