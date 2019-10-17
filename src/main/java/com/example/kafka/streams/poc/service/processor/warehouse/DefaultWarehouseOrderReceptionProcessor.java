package com.example.kafka.streams.poc.service.processor.warehouse;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a warehouse order which stores it in a mongoDB collection.
 */
@Component
public class DefaultWarehouseOrderReceptionProcessor implements WarehouseOrderReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWarehouseOrderReceptionProcessor.class);

    /** The mongoDB repository where to store the generated warehouse orders received */
    private WarehouseOrderRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB generated warehouse order repository
     */
    @Autowired
    public DefaultWarehouseOrderReceptionProcessor(WarehouseOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a generated warehouse order
     *
     * @param order the generated warehouse order received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(WarehouseOrder order) throws ProcessorException {

        try {
            Optional<WarehouseOrderEntity> queryResult = repository.findById(order.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new WarehouseOrderEntity(order));
                LOGGER.info(">>> Warehouse order generated key={} updated in mongoDB", order.getUuid());
            }
            else {
                repository.insert(new WarehouseOrderEntity(order));
                LOGGER.info(">>> Warehouse order generated key={} inserted in mongoDB", order.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a warehouse order generated in the mongoDB database", exc);
        }
    }
}
