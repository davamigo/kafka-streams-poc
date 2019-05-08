package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrderLine;

/**
 * Commercial order line MongoDB entity
 */
public class CommercialOrderLineEntity {

    /** The unique identifier of the order line */
    private String uuid;

    /** The product uuid in the order line */
    private String productUuid;

    /** The unit price for the product in the order line */
    private float price;

    /** The quantity of the product in the order line */
    private int quantity;

    /**
     * Empty constructor
     */
    public CommercialOrderLineEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source commercial order line object
     */
    public CommercialOrderLineEntity(CommercialOrderLine source) {
        this.uuid = source.getUuid();
        this.productUuid = null == source.getProduct() ? null : source.getProduct().getUuid();
        this.price = source.getPrice();
        this.quantity = source.getQuantity();
    }

    /**
     * @return the unique identifier of the order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the product uuid in the order line
     */
    public String getProductUuid() {
        return productUuid;
    }

    /**
     * @return the unit price for the product in the order line
     */
    public float getPrice() {
        return price;
    }

    /**
     * @return the quantity of the product in the order line
     */
    public int getQuantity() {
        return quantity;
    }
}
