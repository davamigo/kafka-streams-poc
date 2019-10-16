package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Warehouse order MongoDB repository
 */
public interface WarehouseOrderRepository extends MongoRepository<WarehouseOrderEntity, String> {
}
