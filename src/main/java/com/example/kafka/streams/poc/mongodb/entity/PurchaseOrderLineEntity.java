package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;

/**
 * Purchase order line MongoDB entity
 */
public class PurchaseOrderLineEntity {

    /** The key of purchase order line: country+date+product-uuid */
    private String key;

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
        this.key = source.getKey();
        this.productUuid = source.getProductUuid();
        this.price = source.getPrice();
        this.quantity = source.getQuantity();
    }

    /**
     * @return the key of purchase order line: country+date+product-uuid
     */
    public String getKey() {
        return key;
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
