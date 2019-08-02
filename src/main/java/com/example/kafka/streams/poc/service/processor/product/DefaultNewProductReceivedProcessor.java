package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import com.example.kafka.streams.poc.mongodb.entity.ProductEntity;
import com.example.kafka.streams.poc.mongodb.repository.ProductRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a new Product which stores the product in a mongoDB collection.
 */
@Component
public class DefaultNewProductReceivedProcessor implements NewProductReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNewProductReceivedProcessor.class);

    /** The mongoDB repository where to store the products received */
    private ProductRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB product repository
     */
    @Autowired
    public DefaultNewProductReceivedProcessor(ProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a product
     *
     * @param product the product received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(Product product) throws ProcessorException {
        try {
            Optional<ProductEntity> queryResult = repository.findById(product.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new ProductEntity(product));
                LOGGER.info(">>> Product uuid={} updated in mongoDB", product.getUuid());
            }
            else {
                repository.insert(new ProductEntity(product));
                LOGGER.info(">>> Product uuid={} inserted in mongoDB", product.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a product in the mongoDB database", exc);
        }
    }
}
