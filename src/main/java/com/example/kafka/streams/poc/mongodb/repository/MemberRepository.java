package com.example.kafka.streams.poc.mongodb.repository;

import com.example.kafka.streams.poc.mongodb.entity.MemberEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Member MongoDB repository
 */
public interface MemberRepository extends MongoRepository<MemberEntity, String> {
}
