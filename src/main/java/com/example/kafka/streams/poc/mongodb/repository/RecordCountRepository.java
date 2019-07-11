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

    /** The mongoDB repository where to retrieve the purchase orders */
    private final PurchaseOrderRepository purchaseOrderRepository;
    /**
     * Autowired constructor
     *
     * @param productRepository the mongoDB product repository
     * @param memberRepository the mongoDB member repository
     * @param commercialOrderRepository the mongoDB commercial order repository
     * @param purchaseOrderRepository the mongoDB purchase order repository
     */
    @Autowired
    public RecordCountRepository(
            ProductRepository productRepository,
            MemberRepository memberRepository,
            CommercialOrderRepository commercialOrderRepository,
            PurchaseOrderRepository purchaseOrderRepository
    ) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.commercialOrderRepository = commercialOrderRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
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
        result.put("purchase-orders", countPurchaseOrders());
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
}
