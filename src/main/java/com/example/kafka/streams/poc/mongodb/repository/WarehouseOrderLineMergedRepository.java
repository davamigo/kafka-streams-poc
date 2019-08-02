package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineMergedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Merged warehouse order line MongoDB repository
 */
public interface WarehouseOrderLineMergedRepository extends MongoRepository<WarehouseOrderLineMergedEntity, String> {
}
