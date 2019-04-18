package com.example.kafka.streams.poc.domain.entity.address;

import com.example.kafka.streams.poc.schemas.member.MemberAddress;
import com.example.kafka.streams.poc.schemas.order.CommercialOrderAddress;

import java.util.Objects;

/**
 * Address domain entity
 */
public class Address {

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
     * Default constructor
     */
    public Address() {
        this.country = null;
        this.state = null;
        this.city = null;
        this.zipCode = null;
        this.street = null;
        this.number = null;
        this.extra = null;
    }

    /**
     * Test constructor
     *
     * @param country the Alpha-2 ISO 3166 country code
     * @param state   the state, province or department
     * @param city    the name of the city
     * @param zipCode the zip code
     * @param street  the name of the street
     * @param number  the number in the street
     * @param extra   the extra data of the address: stairs, floor, door, etc.
     */
    public Address(String country, String state, String city, String zipCode, String street, String number, String extra) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.extra = extra;
    }

    /**
     * @return the country code
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
     * @return the number of the street
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the extra optional data
     */
    public String getExtra() {
        return extra;
    }


    /**
     * Two addresses are the same if they both have the same params
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Address)) {
            return false;
        }

        Address address = (Address) obj;
        return Objects.equals(this.country, address.country)
                && Objects.equals(this.state, address.state)
                && Objects.equals(this.city, address.city)
                && Objects.equals(this.zipCode, address.zipCode)
                && Objects.equals(this.street, address.street)
                && Objects.equals(this.number, address.number)
                && Objects.equals(this.extra, address.extra);
    }

    /**
     * Create new empty address builder
     *
     * @return a new address builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Create new avro exporter
     *
     * @param address the source address
     * @return The Avro exporter
     */
    public static AvroExporter newAvroExporter(Address address) {
        return new AvroExporter(address);
    }

    /**
     * Address builder
     */
    public static class Builder {

        /**
         * The address object
         */
        private Address address;

        /**
         * Default constructor
         */
        private Builder() {
            this.address = new Address();
        }

        /**
         * Builds the address object
         *
         * @return the Address object
         */
        public Address build() {
            return address;
        }

        /**
         * Copy data from an Address object
         *
         * @param address the source address
         * @return this
         */
        public Builder set(Address address) {
            return this
                    .setCountry(address.getCountry())
                    .setState(address.getState())
                    .setCity(address.getCity())
                    .setZipCode(address.getZipCode())
                    .setStreet(address.getStreet())
                    .setNumber(address.getNumber())
                    .setExtra(address.getExtra());
        }

        /**
         * Copy data from a MemberAddress avro object
         *
         * @param address the source address
         * @return this
         */
        public Builder set(MemberAddress address) {
            return this
                    .setCountry(address.getCountry())
                    .setState(address.getState())
                    .setCity(address.getCity())
                    .setZipCode(address.getZipCode())
                    .setStreet(address.getStreet())
                    .setNumber(address.getNumber())
                    .setExtra(address.getExtra());
        }

        /**
         * Copy data from a CommercialOrderAddress avro object
         *
         * @param address the source address
         * @return this
         */
        public Builder set(CommercialOrderAddress address) {
            return this
                    .setCountry(address.getCountry())
                    .setState(address.getState())
                    .setCity(address.getCity())
                    .setZipCode(address.getZipCode())
                    .setStreet(address.getStreet())
                    .setNumber(address.getNumber())
                    .setExtra(address.getExtra());
        }

        /**
         * @param country the country code
         * @return this
         */
        public Builder setCountry(String country) {
            this.address.country = country;
            return this;
        }

        /**
         * @param state the state, province or department
         * @return this
         */
        public Builder setState(String state) {
            this.address.state = state;
            return this;
        }

        /**
         * @param city the name of the city
         * @return this
         */
        public Builder setCity(String city) {
            this.address.city = city;
            return this;
        }

        /**
         * @param zipCode the zip code
         * @return this
         */
        public Builder setZipCode(String zipCode) {
            this.address.zipCode = zipCode;
            return this;
        }

        /**
         * @param street the name of the street
         * @return this
         */
        public Builder setStreet(String street) {
            this.address.street = street;
            return this;
        }

        /**
         * @param number the number of the street
         * @return this
         */
        public Builder setNumber(String number) {
            this.address.number = number;
            return this;
        }

        /**
         * @param extra the extra optional data
         * @return this
         */
        public Builder setExtra(String extra) {
            this.address.extra = extra;
            return this;
        }
    }

    /**
     * Avro exporter
     */
    public static class AvroExporter {

        /** The address object */
        private Address address;

        /**
         * Constructor
         *
         * @param address the source address
         */
        private AvroExporter(Address address) {
            this.address = address;
        }

        /**
         * @return the MemberAddress object
         */
        public MemberAddress exportMemberAddress() {
            return MemberAddress.newBuilder()
                    .setCountry(address.getCountry())
                    .setState(address.getState())
                    .setCity(address.getCity())
                    .setZipCode(address.getZipCode())
                    .setStreet(address.getStreet())
                    .setNumber(address.getNumber())
                    .setExtra(address.getExtra())
                    .build();
        }

        /**
         * @return the CommercialOrderAddress object
         */
        public CommercialOrderAddress exportCommercialOrderAddress() {
            return CommercialOrderAddress.newBuilder()
                    .setCountry(address.getCountry())
                    .setState(address.getState())
                    .setCity(address.getCity())
                    .setZipCode(address.getZipCode())
                    .setStreet(address.getStreet())
                    .setNumber(address.getNumber())
                    .setExtra(address.getExtra())
                    .build();
        }
    }
}
