package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Purchase order MongoDB repository
 */
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrderEntity, String> {
}
