package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Unmatched Warehouse order MongoDB entity
 */
@Document(collection="unmatchedWarehouseOrderLine")
public class WarehouseOrderLineUnmatchedEntity extends WarehouseOrderLineEntity {

    /**
     * Empty constructor
     */
    public WarehouseOrderLineUnmatchedEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order object
     */
    public WarehouseOrderLineUnmatchedEntity(WarehouseOrderLine source) {
        super(source);
    }
}
