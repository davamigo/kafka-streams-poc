package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.mongodb.entity.ProductLegacyIdEntity;
import com.example.kafka.streams.poc.mongodb.repository.ProductLegacyIdRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a product legacy id which stores the data in a mongoDB collection.
 */
@Component
public class DefaultProductLegacyIdReceivedProcessor implements ProductLegacyIdReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProductLegacyIdReceivedProcessor.class);

    /** The mongoDB repository where to store the product legacy ids received */
    private ProductLegacyIdRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB product legacy id repository
     */
    @Autowired
    public DefaultProductLegacyIdReceivedProcessor(ProductLegacyIdRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a product legacy id
     *
     * @param uuid the unique identifier of the product
     * @param legacyId the legacy id the product
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(String uuid, int legacyId) throws ProcessorException {
        try {
            Optional<ProductLegacyIdEntity> queryResult = repository.findById(uuid);
            if (queryResult.isPresent()) {
                repository.save(new ProductLegacyIdEntity(uuid, legacyId));
                LOGGER.info(">>> Product legacy id uuid={} updated in mongoDB", uuid);
            }
            else {
                repository.insert(new ProductLegacyIdEntity(uuid, legacyId));
                LOGGER.info(">>> Product legacy id uuid={} inserted in mongoDB", uuid);
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a product legacy id in the mongoDB database", exc);
        }
    }
}
