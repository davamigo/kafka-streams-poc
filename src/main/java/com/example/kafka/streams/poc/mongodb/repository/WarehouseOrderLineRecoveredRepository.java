package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineRecoveredEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Recovered warehouse order line MongoDB repository
 */
public interface WarehouseOrderLineRecoveredRepository extends MongoRepository<WarehouseOrderLineRecoveredEntity, String> {
}
