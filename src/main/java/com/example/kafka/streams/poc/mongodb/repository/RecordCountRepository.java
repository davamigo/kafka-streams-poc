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

    /** The mongoDB repository where to retrieve the warehouse order lines */
    private final WarehouseOrderLineRepository warehouseOrderLineRepository;

    /**
     * Autowired constructor
     *
     * @param productRepository                  the mongoDB product repository
     * @param memberRepository                   the mongoDB member repository
     * @param commercialOrderRepository          the mongoDB commercial order repository
     * @param commercialOrderConvertedRepository the mongoDB converted commercial order repository
     * @param commercialOrderLineSplitRepository the mongoDB split commercial order line repository
     * @param purchaseOrderRepository            the mongoDB warehouse order lines repository
     * @param warehouseOrderLineRepository       the mongoDB purchase order repository
     */
    @Autowired
    public RecordCountRepository(
            ProductRepository productRepository,
            MemberRepository memberRepository,
            CommercialOrderRepository commercialOrderRepository,
            CommercialOrderConvertedRepository commercialOrderConvertedRepository,
            CommercialOrderLineSplitRepository commercialOrderLineSplitRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            WarehouseOrderLineRepository warehouseOrderLineRepository
    ) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.commercialOrderRepository = commercialOrderRepository;
        this.commercialOrderLineSplitRepository = commercialOrderLineSplitRepository;
        this.commercialOrderConvertedRepository = commercialOrderConvertedRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.warehouseOrderLineRepository = warehouseOrderLineRepository;
    }
    /**
     * Count the records in all the collections
     *
     * @return a map with the name of the collection and the records
     */
    public Map<String, Long> countRecords() {
        Map<String, Long> result = new HashMap<>();
        result.put("products", countProducts());
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
        result.put("full-warehouse-order-lines", countFullWarehouseOrderLines());
        result.put("warehouse-orders", countWarehouseOrders());
        result.put("products-cache", countProductsCache());
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
        // TODO
        return -1;
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
        // TODO
        return -1;
    }

    /**
     * @return the number of matched warehouse order lines
     */
    synchronized long countMatchedWarehouseOrderLines() {
        // TODO
        return -1;
    }

    /**
     * @return the number of unmatched warehouse order lines
     */
    synchronized long countUnmatchedWarehouseOrderLines() {
        // TODO
        return -1;
    }

    /**
     * @return the number of recovered warehouse order lines
     */
    synchronized long countRecoveredWarehouseOrderLines() {
        // TODO
        return -1;
    }

    /**
     * @return the number of failed warehouse order lines
     */
    synchronized long countFailedWarehouseOrderLines() {
        try {
            return warehouseOrderLineRepository.count();
        }
        catch (Exception exc) {
            return -1;
        }
    }

    /**
     * @return the number of full warehouse order lines
     */
    synchronized long countFullWarehouseOrderLines() {
        // TODO
        return -1;
    }

    /**
     * @return the number of warehouse orders
     */
    synchronized long countWarehouseOrders() {
        // TODO
        return -1;
    }

    /**
     * @return the number items in the products cache
     */
    synchronized long countProductsCache() {
        // TODO
        return -1;
    }
}
