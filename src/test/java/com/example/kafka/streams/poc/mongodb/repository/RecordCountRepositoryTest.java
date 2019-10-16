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
    private ProductLegacyIdRepository productLegacyIdRepository;

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

    @Mock
    private WarehouseOrderLineMatchedRepository warehouseOrderLineMatchedRepository;

    @Mock
    private WarehouseOrderLineUnmatchedRepository warehouseOrderLineUnmatchedRepository;

    @Mock
    private WarehouseOrderLineRecoveredRepository warehouseOrderLineRecoveredRepository;

    @Mock
    private WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository;

    @Mock
    private WarehouseOrderLineMergedRepository warehouseOrderLineMergedRepository;

    @Mock
    private WarehouseOrderRepository warehouseOrderRepository;

    @Test
    public void testCountRecords() {

        RecordCountRepository repo = createTestRepo();
        Map<String, Long> result = repo.countRecords();

        Assert.assertTrue(result.containsKey("products"));
        Assert.assertTrue(result.containsKey("products-cache"));
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
    public void testCountProductsCacheWhenSuccess() {

        when(productLegacyIdRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countProductsCache();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountProductsCacheWhenError() {

        when(productLegacyIdRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countProductsCache();

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

    @Test
    public void testCountWarehouseOrderLinesWhenSuccess() {

        when(warehouseOrderLineRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countWarehouseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrderLinesWhenError() {

        when(warehouseOrderLineRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countWarehouseOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountWarehouseOrderLinesMatchedWhenSuccess() {

        when(warehouseOrderLineMatchedRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countMatchedWarehouseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrderLinesMatchedWhenError() {

        when(warehouseOrderLineMatchedRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countMatchedWarehouseOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountWarehouseOrderLinesUnmatchedWhenSuccess() {

        when(warehouseOrderLineUnmatchedRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countUnmatchedWarehouseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrderLinesUnmatchedWhenError() {

        when(warehouseOrderLineUnmatchedRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countUnmatchedWarehouseOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountWarehouseOrderLinesRecoveredWhenSuccess() {

        when(warehouseOrderLineRecoveredRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countRecoveredWarehouseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrderLinesRecoveredWhenError() {

        when(warehouseOrderLineRecoveredRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countRecoveredWarehouseOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountWarehouseOrderLinesFailedWhenSuccess() {

        when(warehouseOrderLineFailedRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countFailedWarehouseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrderLinesFailedWhenError() {

        when(warehouseOrderLineFailedRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countFailedWarehouseOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountWarehouseOrderLinesMergedWhenSuccess() {

        when(warehouseOrderLineMergedRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countMergedWarehouseOrderLines();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrderLinesMergedWhenError() {

        when(warehouseOrderLineMergedRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countMergedWarehouseOrderLines();

        Assert.assertEquals(result, -1);
    }

    @Test
    public void testCountWarehouseOrderWhenSuccess() {

        when(warehouseOrderRepository.count()).thenReturn(new Long(3));

        RecordCountRepository repo = createTestRepo();
        long result = repo.countWarehouseOrders();

        Assert.assertEquals(result, 3);
    }

    @Test
    public void testCountWarehouseOrdersWhenError() {

        when(warehouseOrderRepository.count()).thenThrow(new IllegalArgumentException());

        RecordCountRepository repo = createTestRepo();
        long result = repo.countWarehouseOrders();

        Assert.assertEquals(result, -1);
    }

    private RecordCountRepository createTestRepo() {
        return new RecordCountRepository(
                productRepository,
                productLegacyIdRepository,
                memberRepository,
                commercialOrderRepository,
                commercialOrderConvertedRepository,
                commercialOrderLineSplitRepository,
                purchaseOrderRepository,
                purchaseOrderLineRepository,
                warehouseOrderLineRepository,
                warehouseOrderLineMatchedRepository,
                warehouseOrderLineUnmatchedRepository,
                warehouseOrderLineRecoveredRepository,
                warehouseOrderLineFailedRepository,
                warehouseOrderLineMergedRepository,
                warehouseOrderRepository
        );

    }
}
