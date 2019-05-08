package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Product MongoDB repository
 */
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
}
