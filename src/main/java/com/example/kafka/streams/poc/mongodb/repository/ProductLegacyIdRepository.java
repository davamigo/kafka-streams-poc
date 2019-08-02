package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.ProductLegacyIdEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Product legacy id MongoDB repository
 */
public interface ProductLegacyIdRepository extends MongoRepository<ProductLegacyIdEntity, String> {
}
