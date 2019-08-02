package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineMergedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineMergedRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a merged warehouse order line which stores it in a mongoDB collection.
 */
@Component
public class DefaultMergedWarehouseOrderLineReceptionProcessor implements WarehouseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMergedWarehouseOrderLineReceptionProcessor.class);

    /** The mongoDB repository where to store the merged warehouse order lines received */
    private WarehouseOrderLineMergedRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB merged warehouse order line repository
     */
    @Autowired
    public DefaultMergedWarehouseOrderLineReceptionProcessor(WarehouseOrderLineMergedRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a merged warehouse order line
     *
     * @param line the merged warehouse order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrderLine line) throws ProcessorException {

        try {
            Optional<WarehouseOrderLineMergedEntity> queryResult = repository.findById(line.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderLineMergedEntity(line));
                LOGGER.info(">>> Warehouse order line merged key={} updated in mongoDB", line.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderLineMergedEntity(line));
                LOGGER.info(">>> Warehouse order line merged key={} inserted in mongoDB", line.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order merged in the mongoDB database", exc);
        }
    }
}
