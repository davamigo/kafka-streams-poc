package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted;
import com.example.kafka.streams.poc.service.processor.commercialorder.ConvertedCommercialOrderReceptionProcessorInterface;
import com.example.kafka.streams.poc.service.processor.exception.ProcessorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Unit tests for ConvertedCommercialOrdersKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestConvertedCommercialOrdersKafkaConsumer {

    @Mock
    ConvertedCommercialOrderReceptionProcessorInterface convertedCommercialOrderReceptionProcessor;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        CommercialOrderConverted order = getTestCommercialOrderConverted();

        // Run the test
        ConvertedCommercialOrdersKafkaConsumer convertedCommercialOrdersKafkaConsumer = new ConvertedCommercialOrdersKafkaConsumer(convertedCommercialOrderReceptionProcessor);
        convertedCommercialOrdersKafkaConsumer.listen(order, ack, "101", "ttt");

        // Assertions
        Mockito.verify(convertedCommercialOrderReceptionProcessor, Mockito.times(1)).process(Mockito.any(
                com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted.class
        ));

        Mockito.verify(ack, Mockito.times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        CommercialOrderConverted order = getTestCommercialOrderConverted();

        Mockito.doThrow(new ProcessorException("_msg_")).when(convertedCommercialOrderReceptionProcessor).process(Mockito.any());

        // Run the test
        ConvertedCommercialOrdersKafkaConsumer convertedCommercialOrdersKafkaConsumer = new ConvertedCommercialOrdersKafkaConsumer(convertedCommercialOrderReceptionProcessor);
        convertedCommercialOrdersKafkaConsumer.listen(order, ack, "101", "ttt");

        // Assertions
        Mockito.verify(convertedCommercialOrderReceptionProcessor, Mockito.times(1)).process(Mockito.any(
                com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted.class
        ));

        Mockito.verify(ack, Mockito.times(0)).acknowledge();
    }

    /**
     * @return A converted commercial order for testing purposes
     */
    private CommercialOrderConverted getTestCommercialOrderConverted() {

        return CommercialOrderConverted
                .newBuilder()
                .setUuid("101")
                .setDatetime(102L)
                .setMemberUuid("103")
                .setMemberFirstName("104")
                .setMemberLastName("105")
                .setShippingCountry("106")
                .setShippingCity("107")
                .setShippingZipCode("108")
                .setTotalAmount(109f)
                .setTotalQuantity(110)
                .build();
    }
}
