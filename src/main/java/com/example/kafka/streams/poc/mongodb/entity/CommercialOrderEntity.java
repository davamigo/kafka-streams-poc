package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLine;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Commercial order MongoDB entity
 */
@Document(collection="commercialOrder")
public class CommercialOrderEntity {

    /** The unique identifier of the order */
    @Id
    private String uuid;

    /** The date and time of when the order was created */
    private Date datetime;

    /** The member uuid of the order (client) */
    private String memberUUid;

    /** The address where to ship the order */
    private AddressEntity shippingAddress;

    /** The billing address */
    private AddressEntity billingAddress;

    /** The order lines */
    private List<CommercialOrderLineEntity> lines;

    /**
     * Empty constructor
     */
    public CommercialOrderEntity() {
        this.lines = new ArrayList<>();
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source commercial order object
     */
    public CommercialOrderEntity(CommercialOrder source) {
        this.uuid = source.getUuid();
        this.datetime = source.getDatetime();
        this.memberUUid = null == source.getMember() ? null : source.getMember().getUuid();
        this.shippingAddress = null == source.getShippingAddress() ? null : new AddressEntity(source.getShippingAddress());
        this.billingAddress = null == source.getBillingAddress() ? null : new AddressEntity(source.getBillingAddress());
        this.lines = new ArrayList<>();
        for (CommercialOrderLine line : source.getLines()) {
            this.lines.add(new CommercialOrderLineEntity(line));
        }
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
     * @return the member uuid of the order (client
     */
    public String getMemberUUid() {
        return memberUUid;
    }

    /**
     * @return the address where to ship the order
     */
    public AddressEntity getShippingAddress() {
        return shippingAddress;
    }

    /**
     * @return the billing address
     */
    public AddressEntity getBillingAddress() {
        return billingAddress;
    }

    /**
     * @return the order lines
     */
    public List<CommercialOrderLineEntity> getLines() {
        return lines;
    }
}
