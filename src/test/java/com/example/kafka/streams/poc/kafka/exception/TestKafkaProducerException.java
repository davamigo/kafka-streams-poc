package com.example.kafka.streams.poc.kafka.exception;

import com.example.kafka.streams.poc.schemas.member.Member;
import com.example.kafka.streams.poc.schemas.order.CommercialOrder;
import com.example.kafka.streams.poc.schemas.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

/**
 * Unit test for KafkaProducerException
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestKafkaProducerException {

    @Test
    public void testCreateProductExceptionWhenSuccess() {

        Product product = new Product("101", "102", "103", "104", 105f);
        String topic = "111";
        Exception cause = new Exception();

        KafkaProducerException exc = new KafkaProducerException(cause, product, topic);

        assertEquals(product, exc.getRecordData());
        assertEquals(topic, exc.getTopic());

        String msg = exc.getMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("101"));
        assertTrue(msg.contains("111"));
        assertTrue(msg.contains("product"));
    }

    @Test
    public void testCreateMemberExceptionWhenSuccess() {

        Member member = new Member("201", "202", "203", null);
        String topic = "211";
        Exception cause = new Exception();

        KafkaProducerException exc = new KafkaProducerException(cause, member, topic);

        assertEquals(member, exc.getRecordData());
        assertEquals(topic, exc.getTopic());

        String msg = exc.getMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("201"));
        assertTrue(msg.contains("211"));
        assertTrue(msg.contains("member"));
    }

    @Test
    public void testCreateCommercialOrderExceptionWhenSuccess() {

        CommercialOrder commercialOrder = new CommercialOrder("301", 202L, "303", null, null, null);
        String topic = "311";
        Exception cause = new Exception();

        KafkaProducerException exc = new KafkaProducerException(cause, commercialOrder, topic);

        assertEquals(commercialOrder, exc.getRecordData());
        assertEquals(topic, exc.getTopic());

        String msg = exc.getMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("301"));
        assertTrue(msg.contains("311"));
        assertTrue(msg.contains("commercial order"));
    }
}
