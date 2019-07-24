package com.example.kafka.streams.poc.domain.entity.commercialorder;

import java.util.Date;
import java.util.Objects;

/**
 * Commercial order line split domain entity
 */
public class CommercialOrderLineSplit {

    /** The unique identifier of the order line */
    private String uuid;

    /** The unique identifier of the commercial order where the line belongs */
    private String commercialOrderUuid;

    /** The date and time of when the commercial order was created */
    private Date commercialOrderDatetime;

    /** The Alpha-2 ISO 3166 country code of the shipping address */
    private String shippingCountry;

    /** The unique identifier of the member (client) */
    private String memberUuid;

    /** The unique identifier of the product of the order line */
    private String productUuid;

    /** The name of the product */
    private String productName;

    /** The product type */
    private String productType;

    /** The optional bar code of the product */
    private String productBarCode;

    /** The cost price of the product */
    private float productPrice;

    /** The sell price of the product in the order line */
    private float orderLinePrice;

    /** The quantity of products for this order line */
    private int quantity;

    /**
     * Default constructor
     */
    public CommercialOrderLineSplit() {
        this.uuid = null;
        this.commercialOrderUuid = null;
        this.commercialOrderDatetime = new Date();
        this.shippingCountry = null;
        this.memberUuid = null;
        this.productUuid = null;
        this.productName = null;
        this.productType = null;
        this.productBarCode = null;
        this. productPrice = 0f;
        this.orderLinePrice = 0f;
        this.quantity = 0;
    }

    /**
     * Test constructor
     *
     * @param uuid                      the unique identifier of the order line
     * @param commercialOrderUuid       the unique identifier of the commercial order where the line belongs
     * @param commercialOrderDatetime   the date and time of when the commercial order was created
     * @param shippingCountry           the Alpha-2 ISO 3166 country code of the shipping address
     * @param memberUuid                the unique identifier of the member (client)
     * @param productUuid               the unique identifier of the product of the order line
     * @param productName               the name of the product
     * @param productType               the product type
     * @param productBarCode            the optional bar code of the product
     * @param productPrice              the cost price of the product
     * @param orderLinePrice            the sell price of the product in the order line
     * @param quantity                  the quantity of products for this order line
     */
    public CommercialOrderLineSplit(
            String uuid,
            String commercialOrderUuid,
            Date commercialOrderDatetime,
            String shippingCountry,
            String memberUuid,
            String productUuid,
            String productName,
            String productType,
            String productBarCode,
            Float productPrice,
            Float orderLinePrice,
            Integer quantity
    ) {
        this.uuid = uuid;
        this.commercialOrderUuid = commercialOrderUuid;
        this.commercialOrderDatetime = commercialOrderDatetime != null ? commercialOrderDatetime : new Date();
        this.shippingCountry = shippingCountry;
        this.memberUuid = memberUuid;
        this.productUuid = productUuid;
        this.productName = productName;
        this.productType = productType;
        this.productBarCode = productBarCode;
        this.productPrice = productPrice != null ? productPrice : 0f;
        this.orderLinePrice = orderLinePrice != null ? orderLinePrice : 0f;
        this.quantity = quantity != null ? quantity : 0;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCommercialOrderUuid() {
        return commercialOrderUuid;
    }

    public Date getCommercialOrderDatetime() {
        return commercialOrderDatetime;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public String getMemberUuid() {
        return memberUuid;
    }

    public String getProductUuid() {
        return productUuid;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductBarCode() {
        return productBarCode;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public float getOrderLinePrice() {
        return orderLinePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Two commercial order lines split are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CommercialOrderLineSplit) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((CommercialOrderLineSplit) obj).uuid);
    }

    /**
     * Create new empty commercial order line split  builder
     *
     * @return a new commercial order line split builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Commercial order line split builder
     */
    public static class Builder {

        /** The commercial order line split object */
        CommercialOrderLineSplit line;

        /**
         * Default constructor
         */
        public Builder() {
            this.line = new CommercialOrderLineSplit();
        }

        /**
         * Builds the commercial order line split object
         *
         * @return the commercial order line split object
         */
        public CommercialOrderLineSplit build() {
            return line;
        }

        public Builder set(CommercialOrderLineSplit line) {
            return this
                    .setUuid(line.getUuid())
                    .setCommercialOrderUuid(line.getCommercialOrderUuid())
                    .setCommercialOrderDatetime(line.getCommercialOrderDatetime())
                    .setShippingCountry(line.getShippingCountry())
                    .setMemberUuid(line.getMemberUuid())
                    .setProductUuid(line.getProductUuid())
                    .setProductName(line.getProductName())
                    .setProductType(line.getProductType())
                    .setProductBarCode(line.getProductBarCode())
                    .setProductPrice(line.getProductPrice())
                    .setOrderLinePrice(line.getOrderLinePrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * Copy data from an Avro commercial order line split object
         *
         * @param line the Avro source commercial order line split
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.order.CommercialOrderLineSplit line) {
            return this
                    .setUuid(line.getUuid())
                    .setCommercialOrderUuid(line.getCommercialOrderUuid())
                    .setCommercialOrderDatetime(new Date(line.getCommercialOrderDatetime()))
                    .setShippingCountry(line.getShippingCountry())
                    .setMemberUuid(line.getMemberUuid())
                    .setProductUuid(line.getProductUuid())
                    .setProductName(line.getProductName())
                    .setProductType(line.getProductType())
                    .setProductBarCode(line.getProductBarCode())
                    .setProductPrice(line.getProductPrice())
                    .setOrderLinePrice(line.getOrderLinePrice())
                    .setQuantity(line.getQuantity());
        }

        public Builder setUuid(String uuid) {
            this.line.uuid = uuid;
            return this;
        }

        public Builder setCommercialOrderUuid(String commercialOrderUuid) {
            this.line.commercialOrderUuid = commercialOrderUuid;
            return this;
        }

        public Builder setCommercialOrderDatetime(Date commercialOrderDatetime) {
            this.line.commercialOrderDatetime = commercialOrderDatetime;
            return this;
        }

        public Builder setShippingCountry(String shippingCountry) {
            this.line.shippingCountry = shippingCountry;
            return this;
        }

        public Builder setMemberUuid(String memberUuid) {
            this.line.memberUuid = memberUuid;
            return this;
        }

        public Builder setProductUuid(String productUuid) {
            this.line.productUuid = productUuid;
            return this;
        }

        public Builder setProductName(String productName) {
            this.line.productName = productName;
            return this;
        }

        public Builder setProductType(String productType) {
            this.line.productType = productType;
            return this;
        }

        public Builder setProductBarCode(String productBarCode) {
            this.line.productBarCode = productBarCode;
            return this;
        }

        public Builder setProductPrice(float productPrice) {
            this.line.productPrice = productPrice;
            return this;
        }

        public Builder setOrderLinePrice(float orderLinePrice) {
            this.line.orderLinePrice = orderLinePrice;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.line.quantity = quantity;
            return this;
        }
    }
}
