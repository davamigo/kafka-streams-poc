package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLineSplit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Commercial order line split MongoDB entity
 */
@Document(collection="commercialOrderLineSplit")
public class CommercialOrderLineSplitEntity {

    /** The unique identifier of the order line */
    @Id
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
     * Empty constructor
     */
    public CommercialOrderLineSplitEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param line the source commercial order line split object
     */
    public CommercialOrderLineSplitEntity(CommercialOrderLineSplit line) {
        this.uuid = line.getUuid();
        this.commercialOrderUuid = line.getCommercialOrderUuid();
        this.commercialOrderDatetime = line.getCommercialOrderDatetime();
        this.shippingCountry = line.getShippingCountry();
        this.memberUuid = line.getMemberUuid();
        this.productUuid = line.getProductUuid();
        this.productName = line.getProductName();
        this.productType = line.getProductType();
        this.productBarCode = line.getProductBarCode();
        this.productPrice = line.getProductPrice();
        this.orderLinePrice = line.getOrderLinePrice();
        this.quantity = line.getQuantity();
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
}
