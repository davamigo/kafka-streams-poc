package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Recovered Warehouse order MongoDB entity
 */
@Document(collection="recoveredWarehouseOrderLine")
public class WarehouseOrderLineRecoveredEntity extends WarehouseOrderLineEntity {

    /**
     * Empty constructor
     */
    public WarehouseOrderLineRecoveredEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order object
     */
    public WarehouseOrderLineRecoveredEntity(WarehouseOrderLine source) {
        super(source);
    }
}
