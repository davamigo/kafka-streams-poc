package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLineCondensed;

/**
 * Warehouse order line condensed MongoDB entity (to be used inside warehouse order entity)
 */
public class WarehouseOrderLineCondensedEntity {

    /** The uuid of warehouse order line */
    private String uuid;

    /** The unique identifier of the product */
    private String productUuid;

    /** The legacy identifier of the product */
    private Integer productLegacyId;

    /** The name of the product */
    private String productName;

    /** The optional bar code of the product */
    private String productBarCode;

    /** The quantity of products for this warehouse order line */
    private int quantity;

    /**
     * Empty constructor
     */
    public WarehouseOrderLineCondensedEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source warehouse order object
     */
    public WarehouseOrderLineCondensedEntity(WarehouseOrderLineCondensed source) {
        this.uuid = source.getUuid();
        this.productUuid = source.getProductUuid();
        this.productLegacyId = source.getProductLegacyId();
        this.productName = source.getProductName();
        this.productBarCode = source.getProductBarCode();
        this.quantity = source.getQuantity();
    }

    /**
     * @return the uuid of warehouse order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the unique identifier of the product
     */
    public String getProductUuid() {
        return productUuid;
    }

    /**
     * @return the legacy identifier of the product
     */
    public Integer getProductLegacyId() {
        return productLegacyId;
    }

    /**
     * @return the name of the product
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @return the optional bar code of the product
     */
    public String getProductBarCode() {
        return productBarCode;
    }

    /**
     * @return the quantity of products for this warehouse order line
     */
    public int getQuantity() {
        return quantity;
    }
}
