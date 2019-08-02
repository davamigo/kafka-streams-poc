package com.example.kafka.streams.poc.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Product legacy id MongoDB entity
 */
@Document(collection="productLegacyId")
public class ProductLegacyIdEntity {

    /** The unique identifier of the product */
    @Id
    private String uuid;

    /** The legacy id the product */
    private int legacyId;

    /**
     * Empty constructor
     */
    public ProductLegacyIdEntity() {
    }

    /**
     * Constructor from data
     *
     * @param uuid the unique identifier of the product
     * @param legacyId the legacy id the product
     */
    public ProductLegacyIdEntity(String uuid, int legacyId) {
        this.uuid = uuid;
        this.legacyId = legacyId;
    }

    /**
     * @return the unique identifier of the product
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the legacy id the product
     */
    public int getLegacyId() {
        return legacyId;
    }
}
