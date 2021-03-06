package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import java.util.Objects;

/**
 * Purchase order line condensed domain entity (to be used inside PurchaseOrder domain entity)
 */
public class PurchaseOrderLineCondensed {

    /** The unique identifier of the purchase order line */
    private String uuid;

    /** The aggregation key of purchase order line: country+date+product-uuid */
    private String aggregationKey;

    /** The unique identifier of the product in the purchase order line */
    private String productUuid;

    /** The unit price for the product in the purchase order line */
    private float price;

    /** The quantity of the product in the purchase order line */
    private int quantity;

    /**
     * Default constructor
     */
    public PurchaseOrderLineCondensed() {
        this.uuid = null;
        this.aggregationKey = null;
        this.productUuid = null;
        this.price = 0.0f;
        this.quantity = 1;
    }

    /**
     * Test constructor
     *
     * @param uuid           the unique identifier of the purchase order line
     * @param aggregationKey the aggregation key of purchase order line: country+date+product-uuid
     * @param productUuid    the unique identifier of the product in the purchase order line
     * @param price          the unit price for the product in the purchase order line
     * @param quantity       the quantity of the product in the purchase order line
     */
    public PurchaseOrderLineCondensed(
            String uuid,
            String aggregationKey,
            String productUuid,
            float price,
            int quantity
    ) {
        this.uuid = uuid;
        this.aggregationKey = aggregationKey;
        this.productUuid = productUuid;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * @return the unique identifier of the purchase order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the aggregation key of purchase order line: country+date+product-uuid
     */
    public String getAggregationKey() {
        return aggregationKey;
    }

    /**
     * @return the unique identifier of the product in the purchase order line
     */
    public String getProductUuid() {
        return productUuid;
    }

    /**
     * @return the unit price for the product in the purchase order line
     */
    public float getPrice() {
        return price;
    }

    /**
     * @return the quantity of the product in the purchase order line
     */
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

        if (!(obj instanceof PurchaseOrderLineCondensed) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((PurchaseOrderLineCondensed) obj).uuid);
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
        private PurchaseOrderLineCondensed line;

        /**
         * Default constructor
         */
        public Builder() {
            this.line = new PurchaseOrderLineCondensed();
        }

        /**
         * Builds the purchase order line object
         *
         * @return the purchase order line object
         */
        public PurchaseOrderLineCondensed build() {
            return line;
        }

        /**
         * Copy data from a purchase order line object
         *
         * @param line the source purchase order line
         * @return this
         */
        public Builder set(PurchaseOrderLineCondensed line) {
            return this
                    .setUuid(line.getUuid())
                    .setAggregationKey(line.getAggregationKey())
                    .setProductUuid(line.getProductUuid())
                    .setPrice(line.getPrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * Copy data from an Avro purchase order line object
         *
         * @param line the Avro source purchase order line
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed line) {
            return this
                    .setUuid(line.getUuid())
                    .setAggregationKey(line.getAggregationKey())
                    .setProductUuid(line.getProductUuid())
                    .setPrice(line.getPrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * @param uuid the unique identifier of the purchase order line
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.line.uuid = uuid;
            return this;
        }

        /**
         * @param aggregationKey the aggregation key of purchase order line: country+date+product-uuid
         * @return this
         */
        public Builder setAggregationKey(String aggregationKey) {
            this.line.aggregationKey = aggregationKey;
            return this;
        }

        /**
         * @param productUuid the unique identifier of the product in the purchase order line
         * @return this
         */
        public Builder setProductUuid(String productUuid) {
            this.line.productUuid = productUuid;
            return this;
        }

        /**
         * @param price the unit price for the product in the purchase order line
         * @return this
         */
        public Builder setPrice(float price) {
            this.line.price = price;
            return this;
        }

        /**
         * @param quantity the quantity of the product in the purchase order line
         * @return this
         */
        public Builder setQuantity(int quantity) {
            this.line.quantity = quantity;
            return this;
        }
    }
}
