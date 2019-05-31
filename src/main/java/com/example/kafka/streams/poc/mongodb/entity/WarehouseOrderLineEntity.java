package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Warehouse order MongoDB entity
 */
@Document(collection="failedWarehouseOrderLine")
public class WarehouseOrderLineEntity {

    /** The uuid of warehouse order line */
    @Id
    private String uuid;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the warehouse order line */
    private Date date;

    /** The unique identifier of the product */
    private String productUuid;

    /** The legacy identifier of the product */
    private Integer productLegacyId;

    /** The name of the product */
    private String productName;

    /** The optional bar code of the product */
    private String productBarCode;

    /** The quantity of products for this warehouse order line */
    private int quantity;

    /**
     * Empty constructor
     */
    public WarehouseOrderLineEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order object
     */
    public WarehouseOrderLineEntity(WarehouseOrderLine source) {
        this.uuid = source.getUuid();
        this.country = source.getCountry();
        this.date = source.getDate();
        this.productUuid = source.getProductUuid();
        this.productLegacyId = source.getProductLegacyId();
        this.productName = source.getProductName();
        this.productBarCode = source.getProductBarCode();
        this.quantity = source.getQuantity();
    }

    /**
     * @return the uuid of warehouse order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the Alpha-2 ISO 3166 country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the date and time of the warehouse order line
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the unique identifier of the product
     */
    public String getProductUuid() {
        return productUuid;
    }

    /**
     * @return the legacy identifier of the product
     */
    public Integer getProductLegacyId() {
        return productLegacyId;
    }

    /**
     * @return the name of the product
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @return the optional bar code of the product
     */
    public String getProductBarCode() {
        return productBarCode;
    }

    /**
     * @return the quantity of products for this warehouse order line
     */
    public int getQuantity() {
        return quantity;
    }
}
