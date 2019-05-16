package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;

/**
 * Purchase order line MongoDB entity
 */
public class PurchaseOrderLineEntity {

    /** The unique identifier of the purchase order line */
    private String uuid;

    /** The aggregation key of purchase order line: country+date+product-uuid */
    private String aggregationKey;

    /** The unique identifier of the product in the purchase order line */
    private String productUuid;

    /** The unit price for the product in the purchase order line */
    private float price;

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
        this.productUuid = source.getProductUuid();
        this.price = source.getPrice();
        this.quantity = source.getQuantity();
    }

    /**
     * @return the unique identifier of the purchase order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the aggregation key of purchase order line: country+date+product-uuid
     */
    public String getAggregationKey() {
        return aggregationKey;
    }

    /**
     * @return the unique identifier of the product in the purchase order line
     */
    public String getProductUuid() {
        return productUuid;
    }

    /**
     * @return the unit price for the product in the purchase order line
     */
    public float getPrice() {
        return price;
    }

    /**
     * @return the quantity of the product in the purchase order line
     */
    public int getQuantity() {
        return quantity;
    }
}
