package com.example.kafka.streams.poc.kafka.consumer;

import com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit;
import com.example.kafka.streams.poc.service.processor.commercialorder.SplitCommercialOrderLineReceptionProcessorInterface;
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
 * Unit tests for SplitCommercialOrderLinesKafkaConsumer class
 */
@SpringBootTest
@DirtiesContext
@RunWith(MockitoJUnitRunner.class)
public class TestSplitCommercialOrderLinessKafkaConsumer {

    @Mock
    SplitCommercialOrderLineReceptionProcessorInterface splitCommercialOrderLineReceptionProcessor;

    @Mock
    Acknowledgment ack;

    @Test
    public void testListenHappyPath() {

        // Prepare test data
        CommercialOrderLineSplit line = getTestCommercialOrderLineSplit();

        // Run the test
        SplitCommercialOrderLinesKafkaConsumer consumer = new SplitCommercialOrderLinesKafkaConsumer(splitCommercialOrderLineReceptionProcessor);
        consumer.listen(line, ack, "101", "ttt");

        // Assertions
        Mockito.verify(splitCommercialOrderLineReceptionProcessor, Mockito.times(1)).process(Mockito.any(
                com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit.class
        ));

        Mockito.verify(ack, Mockito.times(1)).acknowledge();
    }

    @Test
    public void testListenWhenExceptionProcessing() {

        // Prepare test data
        CommercialOrderLineSplit line = getTestCommercialOrderLineSplit();

        Mockito.doThrow(new ProcessorException("_msg_")).when(splitCommercialOrderLineReceptionProcessor).process(Mockito.any());

        // Run the test
        SplitCommercialOrderLinesKafkaConsumer consumer = new SplitCommercialOrderLinesKafkaConsumer(splitCommercialOrderLineReceptionProcessor);
        consumer.listen(line, ack, "101", "ttt");

        // Assertions
        Mockito.verify(splitCommercialOrderLineReceptionProcessor, Mockito.times(1)).process(Mockito.any(
                com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit.class
        ));

        Mockito.verify(ack, Mockito.times(0)).acknowledge();
    }

    /**
     * @return A split commercial order line for testing purposes
     */
    private CommercialOrderLineSplit getTestCommercialOrderLineSplit() {

        return CommercialOrderLineSplit
                .newBuilder()
                .setUuid("101")
                .setCommercialOrderUuid("102")
                .setCommercialOrderDatetime(103L)
                .setShippingCountry("104")
                .setMemberUuid("105")
                .setProductUuid("106")
                .setProductName("107")
                .setProductType("108")
                .setProductBarCode("109")
                .setProductPrice(110f)
                .setOrderLinePrice(111f)
                .setQuantity(112)
                .build();
    }
}
