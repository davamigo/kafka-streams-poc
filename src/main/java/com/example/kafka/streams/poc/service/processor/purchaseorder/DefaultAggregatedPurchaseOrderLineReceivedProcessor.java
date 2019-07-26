package com.example.kafka.streams.poc.service.processor.purchaseorder;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderLineEntity;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderLineRepository;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Service to process the reception of a generated PurchaseOrderLine which stores the purchase order line in a mongoDB collection.
 */
@Component
public class DefaultAggregatedPurchaseOrderLineReceivedProcessor implements AggregatedPurchaseOrderLineReceptionProcessorInterface {

    /** Logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAggregatedPurchaseOrderLineReceivedProcessor.class);

    /** The mongoDB repository where to store the purchase order lines received */
    private PurchaseOrderLineRepository repository;

    /**
     * Autowired constructor
     *
     * @param repository the mongoDB purchase order lines repository
     */
    @Autowired
    public DefaultAggregatedPurchaseOrderLineReceivedProcessor(PurchaseOrderLineRepository repository) {
        this.repository = repository;
    }

    /**
     * Process the reception of a generated purchase order line
     *
     * @param purchaseOrderLine the purchase order line received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(PurchaseOrderLine purchaseOrderLine) throws ProcessorException {
        try {
            Optional<PurchaseOrderLineEntity> queryResult = repository.findById(purchaseOrderLine.getUuid());
            if (queryResult.isPresent()) {
                repository.save(new PurchaseOrderLineEntity(purchaseOrderLine));
                LOGGER.info(">>> Purchase order line key={} updated in mongoDB", purchaseOrderLine.getUuid());
            }
            else {
                repository.insert(new PurchaseOrderLineEntity(purchaseOrderLine));
                LOGGER.info(">>> Purchase order line key={} inserted in mongoDB", purchaseOrderLine.getUuid());
            }
        }
        catch (Exception exc) {
            throw new ProcessorException("An error occurred storing a purchase order line in the mongoDB database", exc);
        }
    }
}
