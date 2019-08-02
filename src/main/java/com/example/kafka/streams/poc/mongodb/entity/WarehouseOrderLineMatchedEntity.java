package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Matched Warehouse order MongoDB entity
 */
@Document(collection="matchedWarehouseOrderLine")
public class WarehouseOrderLineMatchedEntity extends WarehouseOrderLineEntity {

    /**
     * Empty constructor
     */
    public WarehouseOrderLineMatchedEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order object
     */
    public WarehouseOrderLineMatchedEntity(WarehouseOrderLine source) {
        super(source);
    }
}
