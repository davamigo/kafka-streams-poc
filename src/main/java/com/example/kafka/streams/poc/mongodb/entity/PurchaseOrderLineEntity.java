package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Purchase order line MongoDB entity
 */
@Document(collection="purchaseOrderLine")
public class PurchaseOrderLineEntity {

    /** The unique identifier of the purchase order line */
    @Id
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
     * Empty constructor
     */
    public PurchaseOrderLineEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order line object
     */
    public PurchaseOrderLineEntity(PurchaseOrderLine source) {
        this.uuid = source.getUuid();
        this.aggregationKey = source.getAggregationKey();
        this.country = source.getCountry();
        this.date = source.getDate();
        this.productUuid = source.getProductUuid();
        this.productName = source.getProductName();
        this.productType = source.getProductType();
        this.productBarCode = source.getProductBarCode();
        this.productPrice = source.getProductPrice();
        this.quantity = source.getQuantity();
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
}
