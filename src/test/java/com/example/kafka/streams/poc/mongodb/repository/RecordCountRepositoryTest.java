package com.example.kafka.streams.poc.mongodb.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test for record count repository
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RecordCountRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommercialOrderRepository commercialOrderRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    public void testCountRecords() {

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        Map<String, Long> result = repo.countRecords();

        Assert.assertTrue(result.containsKey("products"));
        Assert.assertTrue(result.containsKey("members"));
        Assert.assertTrue(result.containsKey("commercial-orders"));
        Assert.assertTrue(result.containsKey("purchase-orders"));
    }

    @Test
    public void testCountProductsWhenSuccess() {

        when(productRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countProducts();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountProductsWhenEWrror() {

        when(productRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countProducts();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountMembersWhenSuccess() {

        when(memberRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countMembers();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountMembersWhenEWrror() {

        when(memberRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countMembers();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountCommercialOrdersWhenSuccess() {

        when(commercialOrderRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countCommercialOrders();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountCommercialOrdersWhenEWrror() {

        when(commercialOrderRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countCommercialOrders();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountPurchaseOrdersWhenSuccess() {

        when(purchaseOrderRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countPurchaseOrders();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountPurchaseOrdersWhenEWrror() {

        when(purchaseOrderRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = new RecordCountRepository(productRepository, memberRepository, commercialOrderRepository, purchaseOrderRepository);
        long result = repo.countPurchaseOrders();

        Assert.assertEquals(result, -1);
    }
}
