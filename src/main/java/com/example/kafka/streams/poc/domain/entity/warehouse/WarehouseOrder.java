package com.example.kafka.streams.poc.domain.entity.warehouse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Warehouse order domain entity
 */
public class WarehouseOrder {

    /** The uuid of warehouse order */
    private String uuid;

    /** The aggregation key: country+date */
    private String aggregationKey;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the warehouse order */
    private Date date;

    /** The warehouse order lines */
    private List<WarehouseOrderLineCondensed> lines;

    /**
     * Default constructor
     */
    public WarehouseOrder() {
        this.uuid = null;
        this.aggregationKey = null;
        this.country = null;
        this.date = new Date();
        this.lines = new ArrayList<>();
    }

    /**
     * Test constructor
     *
     * @param uuid           the uuid of warehouse order
     * @param aggregationKey the aggregation key: country+date
     * @param country        the Alpha-2 ISO 3166 country code
     * @param date           the date and time of the warehouse order
     * @param lines          the warehouse order lines
     */
    public WarehouseOrder(
            String uuid,
            String aggregationKey,
            String country,
            Date date,
            List<WarehouseOrderLineCondensed> lines
    ) {
        this.uuid = uuid;
        this.aggregationKey = aggregationKey;
        this.country = country;
        this.date = date != null ? date : new Date();
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
    }

    /**
     * @return the uuid of warehouse order
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the aggregation key: country+date
     */
    public String getAggregationKey() {
        return aggregationKey;
    }

    /**
     * @return the Alpha-2 ISO 3166 country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the date and time of the warehouse order
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the warehouse order lines
     */
    public List<WarehouseOrderLineCondensed> getLines() {
        return lines;
    }

    /**
     * Two warehouse orders are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof WarehouseOrder) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((WarehouseOrder) obj).uuid);
    }

    /**
     * Create new empty warehouse order builder
     *
     * @return a new warehouse order builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * WarehouseOrder builder
     */
    public static class Builder {

        /** The WarehouseOrder object */
        private WarehouseOrder order;

        /**
         * Default constructor
         */
        private Builder() {
            this.order = new WarehouseOrder();
        }

        /**
         * Builds the WarehouseOrder object
         *
         * @return the WarehouseOrder object
         */
        public WarehouseOrder build() {
            return order;
        }

        /**
         * Copy data from a WarehouseOrder object
         *
         * @param order the source WarehouseOrder
         * @return this
         */
        public Builder set(WarehouseOrder order) {
            return this
                    .setUuid(order.getUuid())
                    .setAggregationKey(order.getAggregationKey())
                    .setCountry(order.getCountry())
                    .setDate(order.getDate())
                    .setLines(order.getLines());
        }

        /**
         * Copy data from an Avro WarehouseOrder object
         *
         * @param order the Avro source WarehouseOrder
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrder order) {

            Date date = new Date(order.getDate());

            List<WarehouseOrderLineCondensed> lines = new ArrayList<>();
            for (com.example.kafka.streams.poc.schemas.warehouse.WarehouseOrderLineCondensed sourceLine : order.getLines()) {
                lines.add(WarehouseOrderLineCondensed.newBuilder().set(sourceLine).build());
            }

            return this
                    .setUuid(order.getUuid())
                    .setAggregationKey(order.getAggregationKey())
                    .setCountry(order.getCountry())
                    .setDate(date)
                    .setLines(lines);
        }

        /**
         * @param uuid the uuid of warehouse order
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.order.uuid = uuid;
            return this;
        }

        /**
         * @param aggregationKey the aggregation key: country+date
         * @return this
         */
        public Builder setAggregationKey(String aggregationKey) {
            this.order.aggregationKey = aggregationKey;
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
         * @param date the date and time of the warehouse order
         * @return this
         */
        public Builder setDate(Date date) {
            this.order.date = date;
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
        public  Builder addLine(WarehouseOrderLineCondensed line) {
            this.order.lines.add(line);
            return this;
        }

        /**
         * @param lines the warehouse order lines
         * @return this
         */
        public Builder setLines(List<WarehouseOrderLineCondensed> lines) {
            this.order.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
            return this;
        }
    }
}
