package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrder;
import com.example.kafka.streams.poc.domain.entity.purchaseorder.PurchaseOrderLine;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Purchase order MongoDB entity
 */
@Document(collection="purchaseOrder")
public class PurchaseOrderEntity {

    /** The key of purchase order: country+date */
    @Id
    private String key;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the purchase order */
    private Date date;

    /** The total amount or the purchase order lines: SUM(price * quantity) */
    private float totalAmount;

    /** The quantity of the products for this purchase order: SUM(quantity) */
    private int totalQuantity;

    /** The purchase order lines */
    private List<PurchaseOrderLineEntity> lines;

    /**
     * Empty constructor
     */
    public PurchaseOrderEntity() {
        this.lines = new ArrayList<>();
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source purchase order object
     */
    public PurchaseOrderEntity(PurchaseOrder source) {
        this.key = source.getKey();
        this.country = source.getCountry();
        this.date = source.getDate();
        this.totalAmount = source.getTotalAmount();
        this.totalQuantity = source.getTotalQuantity();
        this.lines = new ArrayList<>();
        for (PurchaseOrderLine line : source.getLines()) {
            this.lines.add(new PurchaseOrderLineEntity(line));
        }
    }

    /**
     * @return the key of purchase order: country+date
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the Alpha-2 ISO 3166 country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the date and time of the purchase order
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the total amount or the purchase order lines: SUM(price * quantity)
     */
    public float getTotalAmount() {
        return totalAmount;
    }

    /**
     * @return the quantity of the products for this purchase order: SUM(quantity)
     */
    public int getTotalQuantity() {
        return totalQuantity;
    }

    /**
     * @return the purchase order lines
     */
    public List<PurchaseOrderLineEntity> getLines() {
        return lines;
    }
}
