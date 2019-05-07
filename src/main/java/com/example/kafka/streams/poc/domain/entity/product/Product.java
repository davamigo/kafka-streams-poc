package com.example.kafka.streams.poc.domain.entity.product;

import java.util.Objects;

/**
 * Product domain entity
 */
public class Product {

    /** The unique identifier of the product */
    private String uuid;

    /** The name of the product */
    private String name;

    /** The product type */
    private String type;

    /** The optional bar code of the product */
    private String barCode;

    /** The cost price of the product */
    private float price;

    /**
     * Default constructor
     */
    public Product() {
        this.uuid = null;
        this.name = null;
        this.type = null;
        this.barCode = null;
        this.price = 0.0f;
    }

    /**
     * Test constructor
     *
     * @param uuid    the unique identifier of the product
     * @param name    the name of the product
     * @param type    the product type
     * @param barCode the optional bar code of the product
     * @param price   the cost price of the product
     */
    public Product(String uuid, String name, String type, String barCode, float price) {
        this.uuid = uuid;
        this.name = name;
        this.type = type;
        this.barCode = barCode;
        this.price = price;
    }

    /**
     * @return the unique identifier of the product
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @return the product type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the optional bar code of the product
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return the cost price of the product
     */
    public float getPrice() {
        return price;
    }

    /**
     * Two products are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Product) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((Product) obj).uuid);
    }

    /**
     * Create new empty product builder
     *
     * @return a new product builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Create new avro exporter
     *
     * @param product the source product
     * @return The Avro exporter
     */
    public static AvroExporter newAvroExporter(Product product) {
        return new AvroExporter(product);
    }

    /**
     * Product builder
     */
    public static class Builder {

        /** The product object */
        private Product product;

        /**
         * Default constructor
         */
        private Builder() {
            this.product = new Product();
        }

        /**
         * Builds the product object
         *
         * @return the product object
         */
        public Product build() {
            return product;
        }

        /**
         * Copy data from a Product object
         *
         * @param product the source product
         * @return this
         */
        public Builder set(Product product) {
            return this
                    .setUuid(product.getUuid())
                    .setName(product.getName())
                    .setType(product.getType())
                    .setBarCode(product.getBarCode())
                    .setPrice(product.getPrice());
        }

        /**
         * Copy data from an Avro Product object
         *
         * @param product the Avro source product
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.product.Product product) {
            return this
                    .setUuid(product.getUuid())
                    .setName(product.getName())
                    .setType(product.getType())
                    .setBarCode(product.getBarCode())
                    .setPrice(product.getPrice());
        }

        /**
         * @param uuid the unique identifier of the product
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.product.uuid = uuid;
            return this;
        }

        /**
         * @param name the name of the product
         * @return this
         */
        public Builder setName(String name) {
            this.product.name = name;
            return this;
        }

        /**
         * @param type the product type
         * @return this
         */
        public Builder setType(String type) {
            this.product.type = type;
            return this;
        }

        /**
         * @param barCode thr optional bar code of the product
         * @return this
         */
        public Builder setBarCode(String barCode) {
            this.product.barCode = barCode;
            return this;
        }

        /**
         * @param price the cost price of the product
         * @return this
         */
        public Builder setPrice(float price) {
            this.product.price = price;
            return this;
        }
    }

    /**
     * Avro exporter
     */
    public static class AvroExporter {

        /** The product object */
        private Product product;

        /**
         * Constructor
         *
         * @param product the source product
         */
        private AvroExporter(Product product) {
            this.product = product;
        }

        /**
         * @return the product as Avro Product
         */
        public com.example.kafka.streams.poc.schemas.product.Product export() {
            return com.example.kafka.streams.poc.schemas.product.Product.newBuilder()
                    .setUuid(product.getUuid())
                    .setName(product.getName())
                    .setType(product.getType())
                    .setBarCode(product.getBarCode())
                    .setPrice(product.getPrice())
                    .build();
        }
    }
}
