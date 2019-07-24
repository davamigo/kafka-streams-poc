package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderLineSplitEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Commercial order line split MongoDB repository
 */
public interface CommercialOrderLineSplitRepository extends MongoRepository<CommercialOrderLineSplitEntity, String> {
}
