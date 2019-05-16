package com.example.kafka.streams.poc.service.processor.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder;
import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a generated PurchaseOrder which stores the purchase order in a mongoDB collection.
 */
@Component
public class DefaultGeneratedPurchaseOrderReceivedProcessor implements GeneratedPurchaseOrderReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultGeneratedPurchaseOrderReceivedProcessor.class);

    /** The mongoDB repository where to store the purchase orders received */
    private PurchaseOrderRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB purchase order repository
     */
    @Autowired
    public DefaultGeneratedPurchaseOrderReceivedProcessor(PurchaseOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a generated purchase order
     *
     * @param purchaseOrder the purchase order received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(PurchaseOrder purchaseOrder) throws ProcessorException {
        try {
            Optional<PurchaseOrderEntity> queryResult = repository.findById(purchaseOrder.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new PurchaseOrderEntity(purchaseOrder));
                LOGGER.info(">>> Purchase order key={} updated in mongoDB", purchaseOrder.getUuid());
            }
            else {
                repository.insert(new PurchaseOrderEntity(purchaseOrder));
                LOGGER.info(">>> Purchase order key={} inserted in mongoDB", purchaseOrder.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a purchase order in the mongoDB database", exc);
        }
    }
}
