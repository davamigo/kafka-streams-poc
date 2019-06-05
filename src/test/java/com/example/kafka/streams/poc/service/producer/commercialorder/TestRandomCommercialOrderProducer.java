package com.example.kafka.streams.poc.service.producer.commercialorder;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLine;
import com.example.kafka.streams.poc.domain.entity.product.Product;
import com.example.kafka.streams.poc.kafka.producer.NewCommercialOrdersKafkaProducer;
import com.example.kafka.streams.poc.kafka.producer.NewMembersKafkaProducer;
import com.example.kafka.streams.poc.kafka.producer.NewProductsKafkaProducer;
import com.example.kafka.streams.poc.service.generator.commercialorder.CommercialOrderGeneratorInterface;
import com.example.kafka.streams.poc.service.producer.exception.InvalidArgumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test CommercialOrderProducer
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestRandomCommercialOrderProducer {

    @Mock
    private CommercialOrderGeneratorInterface commercialOrderGenerator;

    @Mock
    private NewCommercialOrdersKafkaProducer newCommercialOrdersKafkaProducer;

    @Mock
    private NewMembersKafkaProducer newMembersKafkaProducer;

    @Mock
    private NewProductsKafkaProducer newProductsKafkaProducer;

    @Test
    public void testRunWhenCountIs1Creates1Order() {

        Address shippingAddress = new Address("101", "102", "103", "104", "105", "106", "107");
        List<Address> addresses = new ArrayList<>();
        addresses.add(shippingAddress);
        Member member = new Member("201", "202", "203", addresses);
        Product product = new Product("301", "302", "303", "304", 305f);
        CommercialOrderLine line = new CommercialOrderLine("401", "501", product, 404f, 405);
        CommercialOrder commercialOrder = CommercialOrder
                .newBuilder()
                .setUuid("101")
                .setDatetime(new Date())
                .setMember(member)
                .setShippingAddress(shippingAddress)
                .addLine(line)
                .build();

        when(commercialOrderGenerator.getCommercialOrder()).thenReturn(commercialOrder);

        RandomCommercialOrderProducer service = new RandomCommercialOrderProducer(
                commercialOrderGenerator,
                newCommercialOrdersKafkaProducer,
                newMembersKafkaProducer,
                newProductsKafkaProducer
        );

        List<CommercialOrder> commercialOrders = service.produce(1);

        assertEquals(1, commercialOrders.size());
        verify(commercialOrderGenerator, times(1)).getCommercialOrder();
        verify(newCommercialOrdersKafkaProducer, times(1)).publish(any());
        verify(newProductsKafkaProducer, times(1)).publish(any());
        verify(newMembersKafkaProducer, times(1)).publish(any());
    }

    @Test
    public void testRunWhenCountIs3Creates3Orders() {

        Address shippingAddress = new Address("101", "102", "103", "104", "105", "106", "107");
        List<Address> addresses = new ArrayList<>();
        addresses.add(shippingAddress);
        Member member = new Member("201", "202", "203", addresses);
        Product product = new Product("301", "302", "303", "304", 305f);
        CommercialOrderLine line = new CommercialOrderLine("401", "501", product, 404f, 405);
        CommercialOrder commercialOrder = CommercialOrder
                .newBuilder()
                .setUuid("101")
                .setDatetime(new Date())
                .setMember(member)
                .setShippingAddress(shippingAddress)
                .addLine(line)
                .addLine(line)
                .build();

        when(commercialOrderGenerator.getCommercialOrder()).thenReturn(commercialOrder);

        RandomCommercialOrderProducer service = new RandomCommercialOrderProducer(
                commercialOrderGenerator,
                newCommercialOrdersKafkaProducer,
                newMembersKafkaProducer,
                newProductsKafkaProducer
        );

        List<CommercialOrder> commercialOrders = service.produce(3);

        assertEquals(3, commercialOrders.size());
        verify(commercialOrderGenerator, times(3)).getCommercialOrder();
        verify(newCommercialOrdersKafkaProducer, times(3)).publish(any());
        verify(newProductsKafkaProducer, times(6)).publish(any());
        verify(newMembersKafkaProducer, times(3)).publish(any());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testRunWhenInvalidArgument() {

        RandomCommercialOrderProducer service = new RandomCommercialOrderProducer(
                commercialOrderGenerator,
                newCommercialOrdersKafkaProducer,
                newMembersKafkaProducer,
                newProductsKafkaProducer
        );

        service.produce(-1);
    }

    @Test(expected = Exception.class)
    public void testRunWhenExceptionPublising() {

        when(commercialOrderGenerator.getCommercialOrder()).thenThrow(new Exception());

        RandomCommercialOrderProducer service = new RandomCommercialOrderProducer(
                commercialOrderGenerator,
                newCommercialOrdersKafkaProducer,
                newMembersKafkaProducer,
                newProductsKafkaProducer
        );

        service.produce(1);
    }
}
