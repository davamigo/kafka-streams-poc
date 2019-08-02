package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineMatchedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Matched warehouse order line MongoDB repository
 */
public interface WarehouseOrderLineMatchedRepository extends MongoRepository<WarehouseOrderLineMatchedEntity, String> {
}
