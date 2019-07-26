package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderLineEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Purchase order line MongoDB repository
 */
public interface PurchaseOrderLineRepository extends MongoRepository<PurchaseOrderLineEntity, String> {
}
