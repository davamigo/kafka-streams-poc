package com.example.kafka.streams.poc.domain.entity.commercialorder;

import java.util.Date;
import java.util.Objects;

/**
 * Commercial order converted domain entity
 */
public class CommercialOrderConverted {

    /** The unique identifier of the order */
    private String uuid;

    /** The date and time of when the order was created */
    private Date datetime;

    /** The unique identifier of the member (client) */
    private String memberUuid;

    /** The first name of the member */
    private String memberFirstName;

    /** The last name of the member */
    private String memberLastName;

    /** The Alpha-2 ISO 3166 country code of the shipping address */
    private String shippingCountry;

    /** The name of the city of the shipping address */
    private String shippingCity;

    /** The zip code of the shipping address */
    private String shippingZipCode;

    /** The total amount or the order lines: SUM(price * quantity) */
    private float totalAmount;

    /** The quantity of the products for this order: SUM(quantity) */
    private int totalQuantity;

    /**
     * Default constructor
     */
    public CommercialOrderConverted() {
        this.uuid = null;
        this.datetime = new Date();
        this.memberUuid = null;
        this.memberFirstName = null;
        this.memberLastName = null;
        this.shippingCountry = null;
        this.shippingCity = null;
        this.shippingZipCode = null;
        this.totalAmount = 0f;
        this.totalQuantity = 0;
    }

    /**
     * Test constructor
     *
     * @param uuid            the unique identifier of the order
     * @param datetime        the date and time of when the order was created
     * @param memberUuid      the unique identifier of the member (client)
     * @param memberFirstName the first name of the member
     * @param memberLastName  the last name of the member
     * @param shippingCountry the Alpha-2 ISO 3166 country code of the shipping address
     * @param shippingCity    the name of the city of the shipping address
     * @param shippingZipCode the zip code of the shipping address
     * @param totalAmount     the total amount or the order lines: SUM(price * quantity)
     * @param totalQuantity   the quantity of the products for this order: SUM(quantity)
     */
    public CommercialOrderConverted(
            String uuid,
            Date datetime,
            String memberUuid,
            String memberFirstName,
            String memberLastName,
            String shippingCountry,
            String shippingCity,
            String shippingZipCode,
            Float totalAmount,
            Integer totalQuantity
    ) {
        this.uuid = uuid;
        this.datetime = datetime != null ? datetime : new Date();
        this.memberUuid = memberUuid;
        this.memberFirstName = memberFirstName;
        this.memberLastName = memberLastName;
        this.shippingCountry = shippingCountry;
        this.shippingCity = shippingCity;
        this.shippingZipCode = shippingZipCode;
        this.totalAmount = totalAmount != null ? totalAmount : 0f;
        this.totalQuantity = totalQuantity != null ? totalQuantity : 0;
    }

    /**
     * @return the unique identifier of the order
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the date and time of when the order was created
     */
    public Date getDatetime() {
        return datetime;
    }

    /**
     * @return the unique identifier of the member (client)
     */
    public String getMemberUuid() {
        return memberUuid;
    }

    /**
     * @return the first name of the member
     */
    public String getMemberFirstName() {
        return memberFirstName;
    }

    /**
     * @return the last name of the member
     */
    public String getMemberLastName() {
        return memberLastName;
    }

    /**
     * @return the Alpha-2 ISO 3166 country code of the shipping address
     */
    public String getShippingCountry() {
        return shippingCountry;
    }

    /**
     * @return the name of the city of the shipping address
     */
    public String getShippingCity() {
        return shippingCity;
    }

    /**
     * @return the zip code of the shipping address
     */
    public String getShippingZipCode() {
        return shippingZipCode;
    }

    /**
     * @return the total amount or the order lines: SUM(price * quantity)
     */
    public float getTotalAmount() {
        return totalAmount;
    }

    /**
     * @return the quantity of the products for this order: SUM(quantity)
     */
    public int getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * Two commercial orders converted are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CommercialOrderConverted) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((CommercialOrderConverted) obj).uuid);
    }

    /**
     * Create new empty commercial order converted builder
     *
     * @return a new commercial order converted builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Commercial order converted builder
     */
    public static class Builder {

        /** The commercial order converted object */
        private CommercialOrderConverted order;

        /**
         * Default constructor
         */
        public Builder() {
            this.order = new CommercialOrderConverted();
        }

        /**
         * Builds the commercial order converted object
         *
         * @return the commercial order converted object
         */
        public CommercialOrderConverted build() {
            return order;
        }

        /**
         * Copy data from a commercial order converted object
         *
         * @param order the source commercial order converted
         * @return this
         */
        public Builder set(CommercialOrderConverted order) {

            return this
                    .setUuid(order.getUuid())
                    .setDatetime(order.getDatetime())
                    .setMemberUuid(order.getMemberUuid())
                    .setMemberFirstName(order.getMemberFirstName())
                    .setMemberLastName(order.getMemberLastName())
                    .setShippingCountry(order.getShippingCountry())
                    .setShippingCity(order.getShippingCity())
                    .setShippingZipCode(order.getShippingZipCode())
                    .setTotalAmount(order.getTotalAmount())
                    .setTotalQuantity(order.getTotalQuantity());
        }

        /**
         * Copy data from an Avro commercial order converted object
         *
         * @param order the Avro source commercial converted order
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.order.CommercialOrderConverted order) {

            Date datetime = new Date(order.getDatetime());

            return this
                    .setUuid(order.getUuid())
                    .setDatetime(datetime)
                    .setMemberUuid(order.getMemberUuid())
                    .setMemberFirstName(order.getMemberFirstName())
                    .setMemberLastName(order.getMemberLastName())
                    .setShippingCountry(order.getShippingCountry())
                    .setShippingCity(order.getShippingCity())
                    .setShippingZipCode(order.getShippingZipCode())
                    .setTotalAmount(order.getTotalAmount())
                    .setTotalQuantity(order.getTotalQuantity());
        }

        /**
         * @param uuid the unique identifier of the order
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.order.uuid = uuid;
            return this;
        }

        /**
         * @param datetime the date and time of when the order was created
         * @return this
         */
        public Builder setDatetime(Date datetime) {
            this.order.datetime = (Date) datetime.clone();
            return this;
        }

        /**
         * @param memberUuid the unique identifier of the member (client)
         * @return this
         */
        public Builder setMemberUuid(String memberUuid) {
            this.order.memberUuid = memberUuid;
            return this;
        }

        /**
         * @param memberFirstName the first name of the member
         * @return this
         */
        public Builder setMemberFirstName(String memberFirstName) {
            this.order.memberFirstName = memberFirstName;
            return this;
        }

        /**
         * @param memberLastName the last name of the member
         * @return this
         */
        public Builder setMemberLastName(String memberLastName) {
            this.order.memberLastName = memberLastName;
            return this;
        }

        /**
         * @param shippingCountry the Alpha-2 ISO 3166 country code of the shipping address
         * @return this
         */
        public Builder setShippingCountry(String shippingCountry) {
            this.order.shippingCountry = shippingCountry;
            return this;
        }

        /**
         * @param shippingCity the name of the city of the shipping address
         * @return this
         */
        public Builder setShippingCity(String shippingCity) {
            this.order.shippingCity = shippingCity;
            return this;
        }

        /**
         * @param shippingZipCode the zip code of the shipping address
         * @return this
         */
        public Builder setShippingZipCode(String shippingZipCode) {
            this.order.shippingZipCode = shippingZipCode;
            return this;
        }

        /**
         * @param totalAmount the total amount or the order lines: SUM(price * quantity)
         * @return this
         */
        public Builder setTotalAmount(float totalAmount) {
            this.order.totalAmount = totalAmount;
            return this;
        }

        /**
         * @param totalQuantity the quantity of the products for this order: SUM(quantity)
         * @return this
         */
        public Builder setTotalQuantity(int totalQuantity) {
            this.order.totalQuantity = totalQuantity;
            return this;
        }
    }
}
