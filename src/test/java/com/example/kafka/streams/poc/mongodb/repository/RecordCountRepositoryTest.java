package com.example.kafka.streams.poc.mongodb.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

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
    private CommercialOrderConvertedRepository commercialOrderConvertedRepository;

    @Mock
    private CommercialOrderLineSplitRepository commercialOrderLineSplitRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    PurchaseOrderLineRepository purchaseOrderLineRepository;

    @Mock
    private WarehouseOrderLineRepository warehouseOrderLineRepository;

    @Test
    public void testCountRecords() {

        RecordCountRepository repo = createTestRepo();
        Map<String, Long> result = repo.countRecords();

        Assert.assertTrue(result.containsKey("products"));
        Assert.assertTrue(result.containsKey("members"));
        Assert.assertTrue(result.containsKey("commercial-orders"));
        Assert.assertTrue(result.containsKey("full-commercial-orders"));
        Assert.assertTrue(result.containsKey("commercial-order-lines"));
        Assert.assertTrue(result.containsKey("purchase-order-lines"));
        Assert.assertTrue(result.containsKey("purchase-orders"));
        Assert.assertTrue(result.containsKey("warehouse-order-lines"));
        Assert.assertTrue(result.containsKey("matched-warehouse-order-lines"));
        Assert.assertTrue(result.containsKey("unmatched-warehouse-order-lines"));
        Assert.assertTrue(result.containsKey("recovered-warehouse-order-lines"));
        Assert.assertTrue(result.containsKey("failed-warehouse-order-lines"));
        Assert.assertTrue(result.containsKey("full-warehouse-order-lines"));
        Assert.assertTrue(result.containsKey("warehouse-orders"));
        Assert.assertTrue(result.containsKey("products-cache"));
    }

    @Test
    public void testCountProductsWhenSuccess() {

        when(productRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countProducts();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountProductsWhenError() {

        when(productRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countProducts();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountMembersWhenSuccess() {

        when(memberRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countMembers();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountMembersWhenError() {

        when(memberRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countMembers();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountCommercialOrdersWhenSuccess() {

        when(commercialOrderRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countCommercialOrders();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountCommercialOrdersWhenError() {

        when(commercialOrderRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countCommercialOrders();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountFullCommercialOrdersWhenSuccess() {

        when(commercialOrderConvertedRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countFullCommercialOrders();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountFullCommercialOrdersWhenError() {

        when(commercialOrderConvertedRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countFullCommercialOrders();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountCommercialOrderLinesWhenSuccess() {

        when(commercialOrderLineSplitRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countCommercialOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountCommercialOrderLinesWhenError() {

        when(commercialOrderLineSplitRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countCommercialOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountPurchaseOrdersWhenSuccess() {

        when(purchaseOrderRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countPurchaseOrders();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountPurchaseOrdersWhenError() {

        when(purchaseOrderRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countPurchaseOrders();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountPurchaseOrderLinesWhenSuccess() {

        when(purchaseOrderLineRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countPurchaseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountPurchaseOrderLinesWhenError() {

        when(purchaseOrderLineRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countPurchaseOrderLines();

        Assert.assertEquals(result, -1);
    }

    private RecordCountRepository createTestRepo() {
        return new RecordCountRepository(
                productRepository,
                memberRepository,
                commercialOrderRepository,
                commercialOrderConvertedRepository,
                commercialOrderLineSplitRepository,
                purchaseOrderRepository,
                purchaseOrderLineRepository,
                warehouseOrderLineRepository
        );

    }
}
