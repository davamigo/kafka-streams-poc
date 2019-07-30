package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineFailedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Failed warehouse order line MongoDB repository
 */
public interface WarehouseOrderLineFailedRepository extends MongoRepository<WarehouseOrderLineFailedEntity, String> {
}
