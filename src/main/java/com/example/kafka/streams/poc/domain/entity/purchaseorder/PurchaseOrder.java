package com.example.kafka.streams.poc.domain.entity.purchaseorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Purchase order domain entity
 */
public class PurchaseOrder {

    /** The key of purchase order: country+date */
    private String key;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the purchase order */
    private Date date;

    /** The total amount or the purchase order lines: SUM(price * quantity) */
    private float totalAmount;

    /** The quantity of the products for this purchase order: SUM(quantity) */
    private int totalQuantity;

    /** The purchase order lines */
    private List<PurchaseOrderLine> lines;

    /**
     * Default constructor
     */
    public PurchaseOrder() {
        this.key = null;
        this.country = null;
        this.date = new Date();
        this.totalAmount = 0f;
        this.totalQuantity = 0;
        this.lines = new ArrayList<>();
    }

    /**
     * Test constructor
     *
     * @param key           the key of purchase order: country+date
     * @param country       the Alpha-2 ISO 3166 country code
     * @param date          the date and time of the purchase order
     * @param totalAmount   the total amount or the purchase order lines: SUM(price * quantity)
     * @param totalQuantity the quantity of the products for this purchase order: SUM(quantity)
     * @param lines         the purchase order lines
     */
    public PurchaseOrder(String key, String country, Date date, float totalAmount, int totalQuantity, List<PurchaseOrderLine> lines) {
        this.key = key;
        this.country = country;
        this.date = date != null ? date : new Date();
        this.totalAmount = totalAmount;
        this.totalQuantity = totalQuantity;
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
    }

    /**
     * @return the key of purchase order: country+date
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the Alpha-2 ISO 3166 country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the date and time of the purchase order
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the total amount or the purchase order lines: SUM(price * quantity)
     */
    public float getTotalAmount() {
        return totalAmount;
    }

    /**
     * @return the quantity of the products for this purchase order: SUM(quantity)
     */
    public int getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * @return the purchase order lines
     */
    public List<PurchaseOrderLine> getLines() {
        return lines;
    }

    /**
     * Two purchase orders are the same if they both have the same key
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof PurchaseOrder) || this.key == null) {
            return false;
        }

        return Objects.equals(this.key, ((PurchaseOrder) obj).key);
    }

    /**
     * Create new empty purchase order builder
     *
     * @return a new purchase order builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Purchase order builder
     */
    public static class Builder {

        /**
         * The purchase order object
         */
        private PurchaseOrder order;

        /**
         * Default constructor
         */
        public Builder() {
            this.order = new PurchaseOrder();
        }

        /**
         * Builds the purchase order object
         *
         * @return the purchase order object
         */
        public PurchaseOrder build() {
            return order;
        }

        /**
         * Copy data from a purchase order object
         *
         * @param order the source purchase order
         * @return this
         */
        public Builder set(PurchaseOrder order) {
            return this
                    .setKey(order.getKey())
                    .setCountry(order.getCountry())
                    .setDate(order.getDate())
                    .setTotalAmount(order.getTotalAmount())
                    .setTotalQuantity(order.getTotalQuantity())
                    .setLines(order.getLines());
        }

        /**
         * Copy data from an Avro purchase order object
         *
         * @param order the Avro source purchase order
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.purchase.PurchaseOrder order) {

            Date date = new Date(order.getDate());

            List<PurchaseOrderLine> lines = new ArrayList<>();
            for (com.example.kafka.streams.poc.schemas.purchase.PurchaseOrderLineCondensed sourceLine : order.getLines()) {
                lines.add(PurchaseOrderLine.newBuilder().set(sourceLine).build());
            }

            return this
                    .setKey(order.getKey())
                    .setCountry(order.getCountry())
                    .setDate(date)
                    .setTotalAmount(order.getTotalAmount())
                    .setTotalQuantity(order.getTotalQuantity())
                    .setLines(lines);
        }

        /**
         * @param key the key of purchase order: country+date
         * @return this
         */
        public Builder setKey(String key) {
            this.order.key = key;
            return this;
        }

        /**
         * @param country the Alpha-2 ISO 3166 country code
         * @return this
         */
        public Builder setCountry(String country) {
            this.order.country = country;
            return this;
        }

        /**
         * @param date the date and time of the purchase order
         * @return this
         */
        public Builder setDate(Date date) {
            this.order.date = (Date) date.clone();
            return this;
        }

        /**
         * @param totalAmount the total amount or the purchase order lines: SUM(price * quantity)
         * @return this
         */
        public Builder setTotalAmount(float totalAmount) {
            this.order.totalAmount = totalAmount;
            return this;
        }

        /**
         * @param totalQuantity the quantity of the products for this purchase order: SUM(quantity)
         * @return this
         */
        public Builder setTotalQuantity(int totalQuantity) {
            this.order.totalQuantity = totalQuantity;
            return this;
        }

        /**
         * Removes all order lines from the order
         *
         * @return this
         */
        public  Builder clearLines() {
            this.order.lines.clear();
            return this;
        }

        /**
         * Adds a new order line to the order
         *
         * @param line the new order line
         * @return this
         */
        public  Builder addLine(PurchaseOrderLine line) {
            this.order.lines.add(line);
            return this;
        }

        /**
         * @param lines the purchase order lines
         * @return this
         */
        public Builder setLines(List<PurchaseOrderLine> lines) {
            this.order.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
            return this;
        }
    }
}
