package com.example.kafka.streams.poc.service.processor.product;

import com.example.kafka.streams.poc.domain.entity.product.Product;
import com.example.kafka.streams.poc.mongodb.entity.ProductEntity;
import com.example.kafka.streams.poc.mongodb.repository.ProductRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a new Product which stores the product in a mongoDB collection.
 */
@Component
public class DefaultNewProductReceivedProcessor implements NewProductReceptionProcessorInterface{

    /** The mongoDB repository where to store the products received */
    private ProductRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB cproduct repository
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
            }
            else {
                repository.insert(new ProductEntity(product));
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred inserting a product in the mongo DB database", exc);
        }
    }
}
