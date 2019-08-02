package com.example.kafka.streams.poc.mongodb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository to count the records in the other repositories
 */
@Component
public class RecordCountRepository {

    /** The mongoDB repository where to retrieve the products */
    private final ProductRepository productRepository;

    /** The mongoDB repository where to retrieve the product legacy ids */
    private final ProductLegacyIdRepository productLegacyIdRepository;

    /** The mongoDB repository where to retrieve the members */
    private final MemberRepository memberRepository;

    /** The mongoDB repository where to retrieve the commercial orders */
    private final CommercialOrderRepository commercialOrderRepository;

    /** The mongoDB repository where to retrieve the converted commercial orders */
    private final CommercialOrderConvertedRepository commercialOrderConvertedRepository;

    /** The mongoDB repository where to retrieve the split commercial order lines */
    private final CommercialOrderLineSplitRepository commercialOrderLineSplitRepository;

    /** The mongoDB repository where to retrieve the purchase orders */
    private final PurchaseOrderRepository purchaseOrderRepository;

    /** The mongoDB repository where to retrieve the purchase order lines */
    private final PurchaseOrderLineRepository purchaseOrderLineRepository;

    /** The mongoDB repository where to retrieve the generated warehouse order lines */
    private final WarehouseOrderLineRepository warehouseOrderLineRepository;

    /** The mongoDB repository where to retrieve the matched warehouse order lines */
    private final WarehouseOrderLineMatchedRepository warehouseOrderLineMatchedRepository;

    /** The mongoDB repository where to retrieve the unmatched warehouse order lines */
    private final WarehouseOrderLineUnmatchedRepository warehouseOrderLineUnmatchedRepository;

    /** The mongoDB repository where to retrieve the recovered warehouse order lines */
    private final WarehouseOrderLineRecoveredRepository warehouseOrderLineRecoveredRepository;

    /** The mongoDB repository where to retrieve the failed warehouse order lines */
    private final WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository;

    /** The mongoDB repository where to retrieve the merged warehouse order lines */
    private final WarehouseOrderLineMergedRepository warehouseOrderLineMergedRepository;

    /**
     * Autowired constructor
     *
     * @param productRepository                     the mongoDB product repository
     * @param productLegacyIdRepository             the mongoDB product legacy id repository
     * @param memberRepository                      the mongoDB member repository
     * @param commercialOrderRepository             the mongoDB commercial order repository
     * @param commercialOrderConvertedRepository    the mongoDB converted commercial order repository
     * @param commercialOrderLineSplitRepository    the mongoDB split commercial order line repository
     * @param purchaseOrderRepository               the mongoDB purchase order repository
     * @param purchaseOrderLineRepository           the mongoDB purchase order lines repository
     * @param warehouseOrderLineRepository          the mongoDB warehouse order repository
     * @param warehouseOrderLineMatchedRepository   the mongoDB matched warehouse order repository
     * @param warehouseOrderLineUnmatchedRepository the mongoDB unmatched warehouse order repository
     * @param warehouseOrderLineRecoveredRepository the mongoDB recovered warehouse order repository
     * @param warehouseOrderLineFailedRepository    the mongoDB failed warehouse order repository
     * @param warehouseOrderLineMergedRepository    the mongoDB merged warehouse order repository
     */
    @Autowired
    public RecordCountRepository(
            ProductRepository productRepository,
            ProductLegacyIdRepository productLegacyIdRepository,
            MemberRepository memberRepository,
            CommercialOrderRepository commercialOrderRepository,
            CommercialOrderConvertedRepository commercialOrderConvertedRepository,
            CommercialOrderLineSplitRepository commercialOrderLineSplitRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderLineRepository purchaseOrderLineRepository,
            WarehouseOrderLineRepository warehouseOrderLineRepository,
            WarehouseOrderLineMatchedRepository warehouseOrderLineMatchedRepository,
            WarehouseOrderLineUnmatchedRepository warehouseOrderLineUnmatchedRepository,
            WarehouseOrderLineRecoveredRepository warehouseOrderLineRecoveredRepository,
            WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository,
            WarehouseOrderLineMergedRepository warehouseOrderLineMergedRepository
    ) {
        this.productRepository = productRepository;
        this.productLegacyIdRepository = productLegacyIdRepository;
        this.memberRepository = memberRepository;
        this.commercialOrderRepository = commercialOrderRepository;
        this.commercialOrderLineSplitRepository = commercialOrderLineSplitRepository;
        this.commercialOrderConvertedRepository = commercialOrderConvertedRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderLineRepository = purchaseOrderLineRepository;
        this.warehouseOrderLineRepository = warehouseOrderLineRepository;
        this.warehouseOrderLineMatchedRepository = warehouseOrderLineMatchedRepository;
        this.warehouseOrderLineUnmatchedRepository = warehouseOrderLineUnmatchedRepository;
        this.warehouseOrderLineRecoveredRepository = warehouseOrderLineRecoveredRepository;
        this.warehouseOrderLineFailedRepository = warehouseOrderLineFailedRepository;
        this.warehouseOrderLineMergedRepository = warehouseOrderLineMergedRepository;
    }
    /**
     * Count the records in all the collections
     *
     * @return a map with the name of the collection and the records
     */
    public Map<String, Long> countRecords() {
        Map<String, Long> result = new HashMap<>();
        result.put("products", countProducts());
        result.put("products-cache", countProductsCache());
        result.put("members", countMembers());
        result.put("commercial-orders", countCommercialOrders());
        result.put("full-commercial-orders", countFullCommercialOrders());
        result.put("commercial-order-lines", countCommercialOrderLines());
        result.put("purchase-order-lines", countPurchaseOrderLines());
        result.put("purchase-orders", countPurchaseOrders());
        result.put("warehouse-order-lines", countWarehouseOrderLines());
        result.put("matched-warehouse-order-lines", countMatchedWarehouseOrderLines());
        result.put("unmatched-warehouse-order-lines", countUnmatchedWarehouseOrderLines());
        result.put("recovered-warehouse-order-lines", countRecoveredWarehouseOrderLines());
        result.put("failed-warehouse-order-lines", countFailedWarehouseOrderLines());
        result.put("full-warehouse-order-lines", countMergedWarehouseOrderLines());
        result.put("warehouse-orders", countWarehouseOrders());
        return result;
    }

    /**
     * @return the number of products
     */
    synchronized long countProducts() {
        try {
            return productRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number items in the products legacy id cache
     */
    synchronized long countProductsCache() {
        try {
            return productLegacyIdRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of members
     */
    synchronized long countMembers() {
        try {
            return memberRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of commercial orders
     */
    synchronized long countCommercialOrders() {
        try {
            return commercialOrderRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of full commercial orders
     */
    synchronized long countFullCommercialOrders() {
        try {
            return commercialOrderConvertedRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of commercial order lines
     */
    synchronized long countCommercialOrderLines() {
        try {
            return commercialOrderLineSplitRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of purchase order lines
     */
    synchronized long countPurchaseOrderLines() {
        try {
            return purchaseOrderLineRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of purchase orders
     */
    synchronized long countPurchaseOrders() {
        try {
            return purchaseOrderRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of warehouse order lines
     */
    synchronized long countWarehouseOrderLines() {
        try {
            return warehouseOrderLineRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of matched warehouse order lines
     */
    synchronized long countMatchedWarehouseOrderLines() {
        try {
            return warehouseOrderLineMatchedRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of unmatched warehouse order lines
     */
    synchronized long countUnmatchedWarehouseOrderLines() {
        try {
            return warehouseOrderLineUnmatchedRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of recovered warehouse order lines
     */
    synchronized long countRecoveredWarehouseOrderLines() {
        try {
            return warehouseOrderLineRecoveredRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of failed warehouse order lines
     */
    synchronized long countFailedWarehouseOrderLines() {
        try {
            return warehouseOrderLineFailedRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of full warehouse order lines
     */
    synchronized long countMergedWarehouseOrderLines() {
        try {
            return warehouseOrderLineMergedRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of warehouse orders
     */
    synchronized long countWarehouseOrders() {
        // TODO
        return -1;
    }
}
