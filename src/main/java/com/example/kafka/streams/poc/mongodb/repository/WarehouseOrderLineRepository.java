package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Warehouse order line MongoDB repository
 */
public interface WarehouseOrderLineRepository extends MongoRepository<WarehouseOrderLineEntity, String> {
}
