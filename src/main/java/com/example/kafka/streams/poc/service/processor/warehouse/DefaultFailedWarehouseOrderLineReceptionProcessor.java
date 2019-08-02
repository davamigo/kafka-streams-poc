package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineFailedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineFailedRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a failed warehouse order line which stores it in a mongoDB collection.
 */
@Component
public class DefaultFailedWarehouseOrderLineReceptionProcessor implements WarehouseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFailedWarehouseOrderLineReceptionProcessor.class);

    /** The mongoDB repository where to store the failed warehouse order lines received */
    private WarehouseOrderLineFailedRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB failed warehouse order line repository
     */
    @Autowired
    public DefaultFailedWarehouseOrderLineReceptionProcessor(WarehouseOrderLineFailedRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a failed warehouse order line
     *
     * @param line the failed warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrderLine line) throws ProcessorException {

        try {
            Optional<WarehouseOrderLineFailedEntity> queryResult = repository.findById(line.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderLineFailedEntity(line));
                LOGGER.info(">>> Warehouse order line failed key={} updated in mongoDB", line.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderLineFailedEntity(line));
                LOGGER.info(">>> Warehouse order line failed key={} inserted in mongoDB", line.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order failed in the mongoDB database", exc);
        }
    }
}
