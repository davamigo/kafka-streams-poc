package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineRecoveredEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineRecoveredRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a recovered warehouse order line which stores it in a mongoDB collection.
 */
@Component
public class DefaultRecoveredWarehouseOrderLineReceptionProcessor implements WarehouseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRecoveredWarehouseOrderLineReceptionProcessor.class);

    /** The mongoDB repository where to store the recovered warehouse order lines received */
    private WarehouseOrderLineRecoveredRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB recovered warehouse order line repository
     */
    @Autowired
    public DefaultRecoveredWarehouseOrderLineReceptionProcessor(WarehouseOrderLineRecoveredRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a recovered warehouse order line
     *
     * @param line the recovered warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrderLine line) throws ProcessorException {

        try {
            Optional<WarehouseOrderLineRecoveredEntity> queryResult = repository.findById(line.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderLineRecoveredEntity(line));
                LOGGER.info(">>> Warehouse order line recovered key={} updated in mongoDB", line.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderLineRecoveredEntity(line));
                LOGGER.info(">>> Warehouse order line recovered key={} inserted in mongoDB", line.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order recovered in the mongoDB database", exc);
        }
    }
}
