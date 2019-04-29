package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Commercial order MongoDB repository
 */
public interface CommercialOrderRepository extends MongoRepository<CommercialOrderEntity, String> {
}
