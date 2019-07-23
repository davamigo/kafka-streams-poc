package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderConvertedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Commercial order converted MongoDB repository
 */
public interface CommercialOrderConvertedRepository extends MongoRepository<CommercialOrderConvertedEntity, String> {
}
