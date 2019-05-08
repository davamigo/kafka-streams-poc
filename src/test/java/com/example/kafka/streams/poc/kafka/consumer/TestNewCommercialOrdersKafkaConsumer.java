package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderLine;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import com.example.kafka.streams.poc.service.processor.commercialorder.NewCommercialOrderReceptionProcessorInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NewCommercialOrdersKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestNewCommercialOrdersKafkaConsumer {

    @Mock
    NewCommercialOrderReceptionProcessorInterface newCommercialOrderReceptionProcessorInterface;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        CommercialOrder commercialOrder = getTestCommercialOrder();

        // Run the test
        NewCommercialOrdersKafkaConsumer newCommercialOrdersKafkaConsumer = new NewCommercialOrdersKafkaConsumer(newCommercialOrderReceptionProcessorInterface);
        newCommercialOrdersKafkaConsumer.listen(commercialOrder, ack, "101", "ttt");

        // Assertions
        verify(newCommercialOrderReceptionProcessorInterface, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder.class
        ));

        verify(ack, times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        CommercialOrder commercialOrder = getTestCommercialOrder();

        Mockito.doThrow(new ProcessorException("_msg_")).when(newCommercialOrderReceptionProcessorInterface).process(any());

        // Run the test
        NewCommercialOrdersKafkaConsumer newCommercialOrdersKafkaConsumer = new NewCommercialOrdersKafkaConsumer(newCommercialOrderReceptionProcessorInterface);
        newCommercialOrdersKafkaConsumer.listen(commercialOrder, ack, "101", "ttt");

        // Assertions
        verify(newCommercialOrderReceptionProcessorInterface, times(1)).process(any(
                com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder.class
        ));

        verify(ack, times(0)).acknowledge();
    }

    /**
     * @return A commercial order for testing purposes
     */
    private CommercialOrder getTestCommercialOrder() {

        CommercialOrderAddress shippingAddress = CommercialOrderAddress
                .newBuilder()
                .setCountry("201")
                .setCity("202")
                .setZipCode("203")
                .build();

        List<CommercialOrderLine> lines = new ArrayList<>();

        return CommercialOrder
                .newBuilder()
                .setUuid("101")
                .setDatetime(102)
                .setMemberUuid("103")
                .setShippingAddress(shippingAddress)
                .setLines(lines)
                .build();
    }
}
