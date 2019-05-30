package com.example.kafka.streams.poc.domain.entity.warehouse;

import java.util.Date;
import java.util.Objects;

/**
 * Warehouse order line domain entity
 */
public class WarehouseOrderLine {

    /** The uuid of warehouse order line */
    private String uuid;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the warehouse order line */
    private Date date;

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
    public WarehouseOrderLine() {
        this.uuid = null;
        this.country = null;
        this.date = null;
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
     * @param country         the Alpha-2 ISO 3166 country code
     * @param date            the date and time of the warehouse order line
     * @param productUuid     the unique identifier of the product
     * @param productLegacyId the legacy identifier of the product
     * @param productName     the name of the product
     * @param productBarCode  the optional bar code of the product
     * @param quantity        the quantity of products for this warehouse order line
     */
    public WarehouseOrderLine(
            String uuid,
            String country,
            Date date,
            String productUuid,
            Integer productLegacyId,
            String productName,
            String productBarCode,
            int quantity
    ) {
        this.uuid = uuid;
        this.country = country;
        this.date = date;
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
     * @return the Alpha-2 ISO 3166 country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the date and time of the warehouse order line
     */
    public Date getDate() {
        return date;
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

        if (!(obj instanceof WarehouseOrderLine) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((WarehouseOrderLine) obj).uuid);
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
     * Create new avro exporter
     *
     * @param line the source warehouse order line
     * @return The Avro exporter
     */
    public static AvroExporter newAvroExporter(WarehouseOrderLine line) {
        return new AvroExporter(line);
    }

    /**
     * WarehouseOrderLine builder
     */
    public static class Builder {

        /** The WarehouseOrderLine object */
        private WarehouseOrderLine line;

        /**
         * Default constructor
         */
        private Builder() {
            this.line = new WarehouseOrderLine();
        }

        /**
         * Builds the WarehouseOrderLine object
         *
         * @return the WarehouseOrderLine object
         */
        public WarehouseOrderLine build() {
            return line;
        }

        /**
         * Copy data from a WarehouseOrderLine object
         *
         * @param line the source WarehouseOrderLine
         * @return this
         */
        public Builder set(WarehouseOrderLine line) {
            return this
                    .setUuid(line.getUuid())
                    .setCountry(line.getCountry())
                    .setDate(line.getDate())
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
        public Builder set(com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine line) {
            return this
                    .setUuid(line.getUuid())
                    .setCountry(line.getCountry())
                    .setDate(new Date(line.getDate()))
                    .setProductUuid(line.getProductUuid())
                    .setProductLegacyId(line.getProductLegacyId())
                    .setProductName(line.getProductName())
                    .setProductBarCode(line.getProductBarCode())
                    .setQuantity(line.getQuantity());
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
         * @param country the Alpha-2 ISO 3166 country code
         * @return this
         */
        public Builder setCountry(String country) {
            this.line.country = country;
            return this;
        }

        /**
         * @param date the date and time of the warehouse order line
         * @return this
         */
        public Builder setDate(Date date) {
            this.line.date = date;
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

    /**
     * Avro exporter
     */
    public static class AvroExporter {

        /** The WarehouseOrderLine object */
        private WarehouseOrderLine line;

        /**
         * Constructor
         *
         * @param line the source warehouse order line
         */
        private AvroExporter(WarehouseOrderLine line) {
            this.line = line;
        }

        /**
         * Builds the commercial order line object as Avro commercial order line
         *
         * @return the commercial order line object
         */
        public com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine export() {

            return com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLine.newBuilder()
                    .setUuid(line.getUuid())
                    .setCountry(line.getCountry())
                    .setDate(line.getDate().getTime())
                    .setProductUuid(line.getProductUuid())
                    .setProductLegacyId(line.getProductLegacyId())
                    .setProductName(line.getProductName())
                    .setProductBarCode(line.getProductBarCode())
                    .setQuantity(line.getQuantity())
                    .build();
        }
    }
}
