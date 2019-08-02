package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Merged Warehouse order MongoDB entity
 */
@Document(collection="mergedWarehouseOrderLine")
public class WarehouseOrderLineMergedEntity extends WarehouseOrderLineEntity {

    /**
     * Empty constructor
     */
    public WarehouseOrderLineMergedEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order object
     */
    public WarehouseOrderLineMergedEntity(WarehouseOrderLine source) {
        super(source);
    }
}
