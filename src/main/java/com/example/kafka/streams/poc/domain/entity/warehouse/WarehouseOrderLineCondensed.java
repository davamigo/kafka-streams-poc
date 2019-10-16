package com.example.kafka.streams.poc.domain.entity.warehouse;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineCondensedEntity;

import java.util.Objects;

/**
 * Warehouse order line condensed domain entity (to be used inside WarehouseOrder domain entity)
 */
public class WarehouseOrderLineCondensed {

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
     * Default constructor
     */
    public WarehouseOrderLineCondensed() {
        this.uuid = null;
        this.productUuid = null;
        this.productLegacyId = null;
        this.productName = null;
        this.productBarCode = null;
        this.quantity = 0;
    }

    /**
     * Test constructor
     *
     * @param uuid            the uuid of warehouse order line
     * @param productUuid     the unique identifier of the product
     * @param productLegacyId the legacy identifier of the product
     * @param productName     the name of the product
     * @param productBarCode  the optional bar code of the product
     * @param quantity        the quantity of products for this warehouse order line
     */
    public WarehouseOrderLineCondensed(
            String uuid,
            String productUuid,
            Integer productLegacyId,
            String productName,
            String productBarCode,
            int quantity
    ) {
        this.uuid = uuid;
        this.productUuid = productUuid;
        this.productLegacyId = productLegacyId;
        this.productName = productName;
        this.productBarCode = productBarCode;
        this.quantity = quantity;
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

    /**
     * Two warehouse order lines are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof WarehouseOrderLineCondensed) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((WarehouseOrderLineCondensed) obj).uuid);
    }

    /**
     * Create new empty warehouse order line builder
     *
     * @return a new warehouse order line builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * WarehouseOrderLine builder
     */
    public static class Builder {

        /** The WarehouseOrderLine object */
        private WarehouseOrderLineCondensed line;

        /**
         * Default constructor
         */
        private Builder() {
            this.line = new WarehouseOrderLineCondensed();
        }

        /**
         * Builds the WarehouseOrderLine object
         *
         * @return the WarehouseOrderLine object
         */
        public WarehouseOrderLineCondensed build() {
            return line;
        }

        /**
         * Copy data from a WarehouseOrderLine object
         *
         * @param line the source WarehouseOrderLine
         * @return this
         */
        public Builder set(WarehouseOrderLineCondensed line) {
            return this
                    .setUuid(line.getUuid())
                    .setProductUuid(line.getProductUuid())
                    .setProductLegacyId(line.getProductLegacyId())
                    .setProductName(line.getProductName())
                    .setProductBarCode(line.getProductBarCode())
                    .setQuantity(line.getQuantity());
        }

        /**
         * Copy data from an Avro WarehouseOrderLine object
         *
         * @param line the Avro source WarehouseOrderLine
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed line) {
            return this
                    .setUuid(line.getUuid())
                    .setProductUuid(line.getProductUuid())
                    .setProductLegacyId(line.getProductLegacyId())
                    .setProductName(line.getProductName())
                    .setProductBarCode(line.getProductBarCode())
                    .setQuantity(line.getQuantity());
        }

        /**
         * Copy data from a WarehouseOrderLineEntity object
         *
         * @param entity the source WarehouseOrderLine entity
         * @return this
         */
        public Builder set(WarehouseOrderLineCondensedEntity entity) {
            return this
                    .setUuid(entity.getUuid())
                    .setProductUuid(entity.getProductUuid())
                    .setProductLegacyId(entity.getProductLegacyId())
                    .setProductName(entity.getProductName())
                    .setProductBarCode(entity.getProductBarCode())
                    .setQuantity(entity.getQuantity());
        }

        /**
         * @param uuid the uuid of warehouse order line
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.line.uuid = uuid;
            return this;
        }

        /**
         * @param productUuid the unique identifier of the product
         * @return this
         */
        public Builder setProductUuid(String productUuid) {
            this.line.productUuid = productUuid;
            return this;
        }

        /**
         * @param productLegacyId the legacy identifier of the product
         * @return this
         */
        public Builder setProductLegacyId(Integer productLegacyId) {
            this.line.productLegacyId = productLegacyId;
            return this;
        }

        /**
         * @param productName the name of the product
         * @return this
         */
        public Builder setProductName(String productName) {
            this.line.productName = productName;
            return this;
        }

        /**
         * @param productBarCode the optional bar code of the product
         * @return this
         */
        public Builder setProductBarCode(String productBarCode) {
            this.line.productBarCode = productBarCode;
            return this;
        }

        /**
         * @param quantity the quantity of products for this warehouse order line
         * @return this
         */
        public Builder setQuantity(int quantity) {
            this.line.quantity = quantity;
            return this;
        }
    }
}
