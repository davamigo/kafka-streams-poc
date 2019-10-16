package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrder;
import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLineCondensed;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Warehouse order MongoDB entity
 */
@Document(collection="warehouseOrder")
public class WarehouseOrderEntity {

    /** The uuid of warehouse order line */
    @Id
    private String uuid;

    /** The aggregation key: country+date */
    private String aggregationKey;

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The date and time of the warehouse order line */
    private Date date;

    /** The condensed warehouse order lines */
    private List<WarehouseOrderLineCondensedEntity> lines;

    /**
     * Empty constructor
     */
    public WarehouseOrderEntity() {
        this.lines = new ArrayList<>();
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source warehouse order object
     */
    public WarehouseOrderEntity(WarehouseOrder source) {
        this.uuid = source.getUuid();
        this.aggregationKey = source.getAggregationKey();
        this.country = source.getCountry();
        this.date = source.getDate();
        this.lines = new ArrayList<>();
        for (WarehouseOrderLineCondensed line : source.getLines()) {
            this.lines.add(new WarehouseOrderLineCondensedEntity(line));
        }
    }

    /**
     * @return the uuid of warehouse order line
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the aggregation key: country+date
     */
    public String getAggregationKey() {
        return aggregationKey;
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
     * @return the warehouse order lines
     */
    public List<WarehouseOrderLineCondensedEntity> getLines() {
        return lines;
    }
}
