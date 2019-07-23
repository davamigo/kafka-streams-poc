package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderConverted;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Commercial order converted MongoDB entity
 */
@Document(collection="commercialOrderConverted")
public class CommercialOrderConvertedEntity {

    /** The unique identifier of the commercial order */
    @Id
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
     * Empty constructor
     */
    public CommercialOrderConvertedEntity() {}

    /**
     * Copy constructor from domain entity
     *
     * @param source the source commercial order object
     */
    public CommercialOrderConvertedEntity(CommercialOrderConverted source) {
        this.uuid = source.getUuid();
        this.datetime = source.getDatetime();
        this.memberUuid = source.getMemberUuid();
        this.memberFirstName = source.getMemberFirstName();
        this.memberLastName = source.getMemberLastName();
        this.shippingCountry = source.getShippingCountry();
        this.shippingCity = source.getShippingCity();
        this.shippingZipCode = source.getShippingZipCode();
        this.totalAmount = source.getTotalAmount();
        this.totalQuantity = source.getTotalQuantity();

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
}
