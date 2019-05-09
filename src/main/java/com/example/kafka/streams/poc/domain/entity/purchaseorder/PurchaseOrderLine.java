package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import java.util.Objects;

/**
 * Purchase order line domain entity
 */
public class PurchaseOrderLine {

    /** The key of purchase order line: country+date+product-uuid */
    private String key;

    /** The unique identifier of the product in the purchase order line */
    private String productUuid;

    /** The unit price for the product in the purchase order line */
    private float price;

    /** The quantity of the product in the purchase order line */
    private int quantity;

    /**
     * Default constructor
     */
    public PurchaseOrderLine() {
        this.key = null;
        this.productUuid = null;
        this.price = 0.0f;
        this.quantity = 1;
    }

    /**
     * Test constructor
     *
     * @param key         the key of purchase order line: country+date+product-uuid
     * @param productUuid the unique identifier of the product in the purchase order line
     * @param price       the unit price for the product in the purchase order line
     * @param quantity    the quantity of the product in the purchase order line
     */
    public PurchaseOrderLine(String key, String productUuid, float price, int quantity) {
        this.key = key;
        this.productUuid = productUuid;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * @return the key of purchase order line: country+date+product-uuid
     */
    public String getKey() {
        return key;
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
     * Two purchase order lines are the same if they both have the same key
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof PurchaseOrderLine) || this.key == null) {
            return false;
        }

        return Objects.equals(this.key, ((PurchaseOrderLine) obj).key);
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
                    .setKey(line.getKey())
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
                    .setKey(line.getPurchaseOrderLineKey())
                    .setProductUuid(line.getProductUuid())
                    .setPrice(line.getPrice())
                    .setQuantity(line.getQuantity());
        }

        /**
         * @param key the key of purchase order line: country+date+product-uuid
         * @return this
         */
        public Builder setKey(String key) {
            this.line.key = key;
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
