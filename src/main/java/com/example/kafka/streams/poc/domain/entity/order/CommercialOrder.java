package com.example.kafka.streams.poc.domain.entity.order;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Commercial order domain entity
 */
public class CommercialOrder {

    /** The unique identifier of the order */
    private String uuid;

    /** The date and time of when the order was created */
    private Date datetime;

    /** The member (client) of the order */
    private Member member;

    /** The address where to ship the order */
    private Address shippingAddress;

    /** The billing address */
    private Address billingAddress;

    /** The order lines */
    private List<CommercialOrderLine> lines;

    /**
     * Default constructor
     */
    public CommercialOrder() {
        this.uuid = null;
        this.datetime = new Date();
        this.member = new Member();
        this.shippingAddress = new Address();
        this.billingAddress = null;
        this.lines = new ArrayList<>();
    }

    /**
     * Test constructor
     *
     * @param uuid            the unique identifier of the order
     * @param datetime        the date and time of when the order was created
     * @param member          the member (client) of the order
     * @param shippingAddress the address where to ship the order
     * @param billingAddress  the billing address
     * @param lines           the order lines
     */
    public CommercialOrder(String uuid, Date datetime, Member member, Address shippingAddress, Address billingAddress, List<CommercialOrderLine> lines) {
        this.uuid = uuid;
        this.datetime = datetime != null ? datetime : new Date();
        this.member = member != null ? member : new Member();
        this.shippingAddress = shippingAddress != null ? shippingAddress : new Address();
        this.billingAddress = billingAddress; // Nullable
        this.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
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
     * @return the member (client) of the order
     */
    public Member getMember() {
        return member;
    }

    /**
     * @return the address where to ship the order
     */
    public Address getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @return the billing address
     */
    public Address getBillingAddress() {
        return billingAddress;
    }

    /**
     * @return the order lines
     */
    public List<CommercialOrderLine> getLines() {
        return lines;
    }

    /**
     * Two commercial orders are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof CommercialOrder) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((CommercialOrder) obj).uuid);
    }

    /**
     * Create new empty commercial order builder
     *
     * @return a new commercial order builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Create new avro exporter
     *
     * @param commercialOrder the source commercialOrder
     * @return The Avro exporter
     */
    public static AvroExporter newAvroExporter(CommercialOrder commercialOrder) {
        return new AvroExporter(commercialOrder);
    }

    /**
     * Commercial order builder
     */
    public static class Builder {

        /**
         * The commercial order object
         */
        private CommercialOrder order;

        /**
         * Default constructor
         */
        public Builder() {
            this.order = new CommercialOrder();
        }

        /**
         * Builds the commercial order object
         *
         * @return the commercial order object
         */
        public CommercialOrder build() {
            return order;
        }

        /**
         * Copy data from a commercial order object
         *
         * @param order the source commercial order
         * @return this
         */
        public Builder set(CommercialOrder order) {

            Member member = Member.newBuilder().set(order.getMember()).build();

            Address shippingAddress = Address.newBuilder().set(order.getShippingAddress()).build();

            Address billingAddress = null;
            if (order.getBillingAddress() != null) {
                billingAddress = Address.newBuilder().set(order.getBillingAddress()).build();
            }

            return this
                    .setUuid(order.getUuid())
                    .setDatetime(order.getDatetime())
                    .setMember(member)
                    .setShippingAddress(shippingAddress)
                    .setBillingAddress(billingAddress)
                    .setLines(order.getLines());
        }

        /**
         * Copy data from an Avro commercial order object
         *
         * @param order the Avro source commercial order
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.order.CommercialOrder order) {

            Date datetime = new Date(order.getDatetime());

            Member member = Member.newBuilder().setUuid(order.getMemberUuid()).build();

            Address shippingAddress = null;
            com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress sourceShippingAddress = order.getShippingAddress();
            if (sourceShippingAddress != null) {
                shippingAddress = Address.newBuilder().set(sourceShippingAddress).build();
            }

            Address billingAddress = null;
            com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress sourceBillingAddress = order.getBillingAddress();
            if (sourceBillingAddress != null) {
                billingAddress = Address.newBuilder().set(sourceBillingAddress).build();
            }

            List<CommercialOrderLine> lines = new ArrayList<>();
            for (com.example.kafka.streams.poc.schemas.order.CommercialOrderLine sourceLine : order.getLines()) {
                lines.add(CommercialOrderLine.newBuilder().set(sourceLine).build());
            }

            return this
                    .setUuid(order.getUuid())
                    .setDatetime(datetime)
                    .setMember(member)
                    .setShippingAddress(shippingAddress)
                    .setBillingAddress(billingAddress)
                    .setLines(lines);
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
         * @param member the member (client) of the order
         * @return this
         */
        public Builder setMember (Member member) {
            this.order.member = member != null ? member : new Member();
            return this;
        }

        /**
         * @param shippingAddress the address where to ship the order
         * @return this
         */
        public Builder setShippingAddress(Address shippingAddress) {
            this.order.shippingAddress = shippingAddress != null ? shippingAddress : new Address();
            return this;
        }

        /**
         * @param billingAddress the billing address
         * @return this
         */
        public Builder setBillingAddress(Address billingAddress) {
            this.order.billingAddress = billingAddress;
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
        public  Builder addLine(CommercialOrderLine line) {
            this.order.lines.add(line);
            return this;
        }

        /**
         * @param lines the order lines
         * @return this
         */
        public Builder setLines(List<CommercialOrderLine> lines) {
            this.order.lines = lines != null ? new ArrayList<>(lines) : new ArrayList<>();
            return this;
        }
    }

    /**
     * Avro exporter
     */
    public static class AvroExporter {

        /** The CommercialOrder object */
        private CommercialOrder commercialOrder;

        /**
         * Constructor
         *
         * @param commercialOrder the source commercialOrder
         */
        private AvroExporter(CommercialOrder commercialOrder) {
            this.commercialOrder = commercialOrder;
        }

        /**
         * Builds the commercial order line object as Avro commercial order line
         *
         * @return the commercial order line object
         */
        public com.example.kafka.streams.poc.schemas.order.CommercialOrder export() {

            com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress shippingAddress = null;
            Address sourceShippingAddress = commercialOrder.getShippingAddress();
            if (sourceShippingAddress != null) {
                shippingAddress = Address.newAvroExporter(sourceShippingAddress).exportCommercialOrderAddress();
            }

            com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress billingAddress = null;
            Address sourceBillingAddress = commercialOrder.getBillingAddress();
            if (sourceBillingAddress != null) {
                billingAddress = Address.newAvroExporter(sourceBillingAddress).exportCommercialOrderAddress();
            }

            List<com.example.kafka.streams.poc.schemas.order.CommercialOrderLine> lines = new ArrayList<>();
            for (CommercialOrderLine sourceLine : commercialOrder.getLines()) {
                lines.add(CommercialOrderLine.newAvroExporter(sourceLine).export());
            }

            return com.example.kafka.streams.poc.schemas.order.CommercialOrder.newBuilder()
                    .setUuid(commercialOrder.getUuid())
                    .setDatetime(commercialOrder.getDatetime().getTime())
                    .setMemberUuid(commercialOrder.getMember().getUuid())
                    .setShippingAddress(shippingAddress)
                    .setBillingAddress(billingAddress)
                    .setLines(lines)
                    .build();
        }
    }
}
