package com.example.kafka.streams.poc.domain.entity.commercialorder;

import com.example.kafka.streams.poc.domain.entity.product.Product;

import java.util.Objects;

/**
 * Commercial order line domain entity
 */
public class CommercialOrderLine {

    /** The unique identifier of the order line */
    private String uuid;

    /** The unique identifier of the commercial order where the line belongs */
    private String commercialOrderUuid;

    /** The product in the order line */
    private Product product;

    /** The unit price for the product in the order line */
    private float price;

    /** The quantity of the product in the order line */
    private int quantity;

    /**
     * Default constructor
     */
    public CommercialOrderLine() {
        this.uuid = null;
        this.commercialOrderUuid = null;
        this.product = new Product();
        this.price = 0.0f;
        this.quantity = 1;
    }

    /**
     * Test constructor
     *
     * @param uuid                the unique identifier of the order line
     * @param commercialOrderUuid the unique identifier of the commercial order where the line belongs
     * @param product             the product in the order line
     * @param price               the unit price for the product in the order line
     * @param quantity            the quantity of the product in the order line
     */
    public CommercialOrderLine(String uuid, String commercialOrderUuid, Product product, float price, int quantity) {
        this.uuid = uuid;
        this.commercialOrderUuid = commercialOrderUuid;
        this.product = product != null ? product : new Product();
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * @return the unique identifier of the order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return unique identifier of the commercial order where the line belongs
     */
    public String getCommercialOrderUuid() {
        return commercialOrderUuid;
    }

    /**
     * @return the product in the order line
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @return the unit price for the product in the order line
     */
    public float getPrice() {
        return price;
    }

    /**
     * @return the quantity of the product in the order line
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Two commercial order lines are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CommercialOrderLine) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((CommercialOrderLine) obj).uuid);
    }

    /**
     * Create new empty commercial order line builder
     *
     * @return a new commercial order line builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Create new avro exporter
     *
     * @param commercialOrderLine the source commercialOrderLine
     * @return The Avro exporter
     */
    public static AvroExporter newAvroExporter(CommercialOrderLine commercialOrderLine) {
        return new AvroExporter(commercialOrderLine);
    }

    /**
     * Commercial order line builder
     */
    public static class Builder {

        /** The commercial order line object */
        private CommercialOrderLine line;

        /**
         * Default constructor
         */
        public Builder() {
            this.line = new CommercialOrderLine();
        }

        /**
         * Builds the commercial order line object
         *
         * @return the commercial order line object
         */
        public CommercialOrderLine build() {
            return line;
        }

        /**
         * Copy data from a commercial order line object
         *
         * @param line the source commercial order line
         * @return this
         */
        public Builder set(CommercialOrderLine line) {
            return this
                    .setUuid(line.getUuid())
                    .setCommercialOrderUuid(line.getCommercialOrderUuid())
                    .setProduct(Product.newBuilder().set(line.getProduct()).build())
                    .setPrice(line.getPrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * Copy data from an Avro commercial order line object
         *
         * @param line the Avro source commercial order line
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.order.CommercialOrderLine line) {
            return this
                    .setUuid(line.getUuid())
                    .setCommercialOrderUuid(line.getCommercialOrderUuid())
                    .setProduct(Product.newBuilder().setUuid(line.getProductUuid()).build())
                    .setPrice(line.getPrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * @param uuid the unique identifier of the order line
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.line.uuid = uuid;
            return this;
        }

        /**
         * @param commercialOrderUuid the unique identifier of the commercial order where the line belongs
         * @return this
         */
        public Builder setCommercialOrderUuid(String commercialOrderUuid) {
            this.line.commercialOrderUuid = commercialOrderUuid;
            return this;
        }

        /**
         * @param product the product in the order line
         * @return this
         */
        public Builder setProduct(Product product) {
            this.line.product = product != null ? product : new Product();
            return this;
        }

        /**
         * @param price the unit price for the product in the order line
         * @return this
         */
        public Builder setPrice(float price) {
            this.line.price = price;
            return this;
        }

        /**
         * @param quantity the quantity of the product in the order line
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

        /** The commercialOrderLine object */
        private CommercialOrderLine commercialOrderLine;

        /**
         * Constructor
         *
         * @param commercialOrderLine the source commercialOrderLine
         */
        private AvroExporter(CommercialOrderLine commercialOrderLine) {
            this.commercialOrderLine = commercialOrderLine;
        }

        /**
         * Builds the commercial order line object as Avro commercial order line
         *
         * @return the commercial order line object
         */
        public com.example.kafka.streams.poc.schemas.order.CommercialOrderLine export() {
            return com.example.kafka.streams.poc.schemas.order.CommercialOrderLine.newBuilder()
                    .setUuid(commercialOrderLine.getUuid())
                    .setCommercialOrderUuid(commercialOrderLine.getCommercialOrderUuid())
                    .setProductUuid(commercialOrderLine.getProduct().getUuid())
                    .setPrice(commercialOrderLine.getPrice())
                    .setQuantity(commercialOrderLine.getQuantity())
                    .build();
        }
    }
}
