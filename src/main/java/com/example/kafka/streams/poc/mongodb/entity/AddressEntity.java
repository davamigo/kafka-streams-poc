package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;

/**
 * Address MongoDB entity
 */
public class AddressEntity {

    /** The Alpha-2 ISO 3166 country code */
    private String country;

    /** The state, province or department */
    private String state;

    /** The name of the city */
    private String city;

    /** The zip code */
    private String zipCode;

    /** The name of the street */
    private String street;

    /** The number in the street */
    private String number;

    /** The extra data of the address: stairs, floor, door, etc. */
    private String extra;

    /**
     * Empty constructor
     */
    public AddressEntity() {
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source address object
     */
    public AddressEntity(Address source) {
        this.country = source.getCountry();
        this.state = source.getState();
        this.city = source.getCity();
        this.zipCode = source.getZipCode();
        this.street = source.getStreet();
        this.number = source.getNumber();
        this.extra = source.getExtra();
    }

    /**
     * @return the Alpha-2 ISO 3166 country code
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the state, province or department
     */
    public String getState() {
        return state;
    }

    /**
     * @return the name of the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @return the name of the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @return the number in the street
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the extra data of the address: stairs, floor, door, etc.
     */
    public String getExtra() {
        return extra;
    }
}
