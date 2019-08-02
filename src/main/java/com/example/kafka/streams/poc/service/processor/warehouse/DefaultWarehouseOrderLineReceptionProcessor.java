package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a warehouse order line which stores it in a mongoDB collection.
 */
@Component
public class DefaultWarehouseOrderLineReceptionProcessor implements WarehouseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWarehouseOrderLineReceptionProcessor.class);

    /** The mongoDB repository where to store the generated warehouse order lines received */
    private WarehouseOrderLineRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB generated warehouse order line repository
     */
    @Autowired
    public DefaultWarehouseOrderLineReceptionProcessor(WarehouseOrderLineRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a generated warehouse order line
     *
     * @param line the generated warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrderLine line) throws ProcessorException {

        try {
            Optional<WarehouseOrderLineEntity> queryResult = repository.findById(line.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderLineEntity(line));
                LOGGER.info(">>> Warehouse order line generated key={} updated in mongoDB", line.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderLineEntity(line));
                LOGGER.info(">>> Warehouse order line generated key={} inserted in mongoDB", line.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order generated in the mongoDB database", exc);
        }
    }
}
