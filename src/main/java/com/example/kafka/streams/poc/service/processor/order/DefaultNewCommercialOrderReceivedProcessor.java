package com.example.kafka.streams.poc.service.processor.order;

import com.example.kafka.streams.poc.domain.entity.order.CommercialOrder;
import com.example.kafka.streams.poc.service.processor.ProcessorException;
import org.springframework.stereotype.Component;

/**
 * Service to do process the reception of a new CommercialOrder
 */
@Component
public class DefaultNewCommercialOrderReceivedProcessor implements NewCommercialOrderReceptionProcessorInterface {

    /**
     * Process the reception of a commercial order
     *
     * @param commercialOrder the commercial order received
     * @throws ProcessorException when an error occurred
     */
    @Override
    public void process(CommercialOrder commercialOrder) throws ProcessorException {
        // TODO
    }
}
