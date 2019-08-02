package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineUnmatchedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Unmatched warehouse order line MongoDB repository
 */
public interface WarehouseOrderLineUnmatchedRepository extends MongoRepository<WarehouseOrderLineUnmatchedEntity, String> {
}
