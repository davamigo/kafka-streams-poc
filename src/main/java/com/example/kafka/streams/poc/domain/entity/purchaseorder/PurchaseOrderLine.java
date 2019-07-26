package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import java.util.Date;
import java.util.Objects;

/**
 * Purchase order line domain entity
 */
public class PurchaseOrderLine {

    /** The unique identifier of the purchase order line */
    private String uuid;

    /** The aggregation key of purchase order line: country+date+product-uuid */
    private String aggregationKey;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the purchase order */
    private Date date;

    /** The unique identifier of the product in the purchase order line */
    private String productUuid;

    /** The name of the product */
    private String productName;

    /** The product type */
    private String productType;

    /** The optional bar code of the product */
    private String productBarCode;

    /** The cost price of the product */
    private float productPrice;

    /** The quantity of the product in the purchase order line */
    private int quantity;

    /**
     * Default constructor
     */
    public PurchaseOrderLine() {
        this.uuid = null;
        this.aggregationKey = null;
        this.country = null;
        this.date = new Date();
        this.productUuid = null;
        this.productName = null;
        this.productType = null;
        this.productBarCode = null;
        this.productPrice = 0f;
        this.quantity = 1;
    }

    /**
     * Test constructor
     *
     * @param uuid           the unique identifier of the purchase order line
     * @param aggregationKey the aggregation key of purchase order line: country+date+product-uuid
     * @param country        the Alpha-2 ISO 3166 country code
     * @param date           the date and time of the purchase order
     * @param productUuid    the unique identifier of the product in the purchase order line
     * @param productName    the name of the product
     * @param productType    the product type
     * @param productBarCode the optional bar code of the product
     * @param productPrice   the cost price of the product
     * @param quantity       the quantity of the product in the purchase order line
     */
    public PurchaseOrderLine(
            String uuid,
            String aggregationKey,
            String country,
            Date date,
            String productUuid,
            String productName,
            String productType,
            String productBarCode,
            Float productPrice,
            Integer quantity
    ) {
        this.uuid = uuid;
        this.aggregationKey = aggregationKey;
        this.country = country;
        this.date = date != null ? date : new Date();
        this.productUuid = productUuid;
        this.productName = productName;
        this.productType = productType;
        this.productBarCode = productBarCode;
        this.productPrice = productPrice != null ? productPrice : 0f;
        this.quantity = quantity != null ? quantity : 1;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAggregationKey() {
        return aggregationKey;
    }

    public String getCountry() {
        return country;
    }

    public Date getDate() {
        return date;
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

    public int getQuantity() {
        return quantity;
    }

    /**
     * Two purchase order lines are the same if they both have the same aggregationKey
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof PurchaseOrderLine) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((PurchaseOrderLine) obj).uuid);
    }

    /**
     * Create new empty purchase order line builder
     *
     * @return a new purchase order line builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * Purchase order line builder
     */
    public static class Builder {

        /** The purchase order line object */
        private PurchaseOrderLine line;

        /**
         * Default constructor
         */
        public Builder() {
            this.line = new PurchaseOrderLine();
        }

        /**
         * Builds the purchase order line object
         *
         * @return the purchase order line object
         */
        public PurchaseOrderLine build() {
            return line;
        }

        /**
         * Copy data from a purchase order line object
         *
         * @param line the source purchase order line
         * @return this
         */
        public Builder set(PurchaseOrderLine line) {
            return this
                    .setUuid(line.getUuid())
                    .setAggregationKey(line.getAggregationKey())
                    .setCountry(line.getCountry())
                    .setDate(line.getDate())
                    .setProductUuid(line.getProductUuid())
                    .setProductName(line.getProductName())
                    .setProductType(line.getProductType())
                    .setProductBarCode(line.getProductBarCode())
                    .setProductPrice(line.getProductPrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * Copy data from an Avro purchase order line object
         *
         * @param line the Avro source purchase order line
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLine line) {
            return this
                    .setUuid(line.getUuid())
                    .setAggregationKey(line.getAggregationKey())
                    .setCountry(line.getCountry())
                    .setDate(new Date(line.getDate()))
                    .setProductUuid(line.getProductUuid())
                    .setProductPrice(line.getProductPrice())
                    .setProductName(line.getProductName())
                    .setProductType(line.getProductType())
                    .setProductBarCode(line.getProductBarCode())
                    .setQuantity(line.getQuantity());
        }

        public Builder setUuid(String uuid) {
            this.line.uuid = uuid;
            return this;
        }

        public Builder setAggregationKey(String aggregationKey) {
            this.line.aggregationKey = aggregationKey;
            return this;
        }

        public Builder setCountry(String country) {
            this.line.country = country;
            return this;
        }

        public Builder setDate(Date date) {
            this.line.date = date;
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

        public Builder setQuantity(int quantity) {
            this.line.quantity = quantity;
            return this;
        }
    }
}
