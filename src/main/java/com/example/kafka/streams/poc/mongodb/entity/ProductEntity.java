package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.product.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Product MongoDB entity
 */
@Document(collection="product")
public class ProductEntity {

    /** The unique identifier of the product */
    @Id
    private String uuid;

    /** The name of the product */
    private String name;

    /** The product type */
    private String type;

    /** The optional bar code of the product */
    private String barCode;

    /** The cost price of the product */
    private float price;


    /**
     * Empty constructor
     */
    public ProductEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source product object
     */
    public ProductEntity(Product source) {
        this.uuid = source.getUuid();
        this.name = source.getName();
        this.type = source.getType();
        this.barCode = source.getBarCode();
        this.price = source.getPrice();
    }

    /**
     * @return the unique identifier of the product
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * @return the product type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the optional bar code of the product
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return the cost price of the product
     */
    public float getPrice() {
        return price;
    }
}
