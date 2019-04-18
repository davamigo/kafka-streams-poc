package com.example.kafka.streams.poc.domain.entity.member;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.schemas.member.MemberAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Member domain entity
 */
public class Member {

    /** The unique identifier of the member */
    private String uuid;

    /** The first name of the member */
    private String firstName;

    /** The last name of the member */
    private String lastName;

    /** The addresses of the member */
    private List<Address> addresses;

    /**
     * Default constructor
     */
    public Member() {
        this.uuid = null;
        this.firstName = null;
        this.lastName = null;
        this.addresses = new ArrayList<>();
    }

    /**
     * Test constructor
     *
     * @param uuid      the unique identifier of the member
     * @param firstName the first name of the member
     * @param lastName  the last name of the member
     * @param addresses the addresses of the member
     */
    public Member(String uuid, String firstName, String lastName, List<Address> addresses) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addresses = addresses;
    }

    /**
     * @return the unique id.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the list of addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }


    /**
     * Two members are the same if they both have the same uuid
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Member) || this.uuid == null) {
            return false;
        }

        return Objects.equals(this.uuid, ((Member) obj).uuid);
    }

    /**
     * Create new empty member builder
     *
     * @return a new member builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Create new avro exporter
     *
     * @param member the source member
     * @return The Avro exporter
     */
    public static AvroExporter newAvroExporter(Member member) {
        return new AvroExporter(member);
    }

    /**
     * Member builder
     */
    public static class Builder {

        /**
         * The member object
         */
        private Member member;

        /**
         * Default constructor
         */
        private Builder() {
            this.member = new Member();
        }

        /**
         * Builds the member object
         *
         * @return the Member object
         */
        public Member build() {
            return member;
        }

        /**
         * Copy data from an Member object
         *
         * @param member the source member
         * @return this
         */
        public Builder set(Member member) {
            return this
                    .setUuid(member.getUuid())
                    .setFirstName(member.getFirstName())
                    .setLastName(member.getLastName())
                    .setAddresses(member.getAddresses());
        }

        /**
         * Copy data from an Avro Member object
         *
         * @param member the source member
         * @return this
         */
        public Builder set(com.example.kafka.streams.poc.schemas.member.Member member) {

            List<Address> addresses = new ArrayList<>();
            for (MemberAddress memberAddress : member.getAddresses()) {
                addresses.add(Address.newBuilder().set(memberAddress).build());
            }

            return this
                    .setUuid(member.getUuid())
                    .setFirstName(member.getFirstName())
                    .setLastName(member.getLastName())
                    .setAddresses(addresses);
        }

        /**
         * @param uuid the uuid
         * @return this
         */
        public Builder setUuid(String uuid) {
            this.member.uuid = uuid;
            return this;
        }

        /**
         * @param firstName the first name
         * @return this
         */
        public Builder setFirstName(String firstName) {
            this.member.firstName = firstName;
            return this;
        }

        /**
         * @param lastName the last name
         * @return this
         */
        public Builder setLastName(String lastName) {
            this.member.lastName = lastName;
            return this;
        }

        /**
         * Removes all addresses from the list
         *
         * @return this
         */
        public Builder clearAddress() {
            this.member.addresses.clear();
            return this;
        }

        /**
         * Adds a new address to the list
         *
         * @param address the new address
         * @return this
         */
        public Builder addAddress(Address address) {
            this.member.addresses.add(address);
            return this;
        }

        /**
         * @param addresses the list of addresses
         * @return this
         */
        public Builder setAddresses(List<Address> addresses) {
            this.clearAddress();
            for (Address address : addresses) {
                this.addAddress(address);
            }
            return this;
        }
    }

    /**
     * Avro exporter
     */
    public static class AvroExporter {

        /** The member object */
        private Member member;

        /**
         * Constructor
         *
         * @param member the source member
         */
        private AvroExporter(Member member) {
            this.member = member;
        }

        /**
         * @return the avro member
         */
        public com.example.kafka.streams.poc.schemas.member.Member export() {

            List<MemberAddress> memberAddresses = new ArrayList<>();
            for (Address address : this.member.getAddresses()) {
                memberAddresses.add(Address.newAvroExporter(address).exportMemberAddress());
            }

            return com.example.kafka.streams.poc.schemas.member.Member.newBuilder()
                    .setUuid(this.member.getUuid())
                    .setFirstName(this.member.getFirstName())
                    .setLastName(this.member.getLastName())
                    .setAddresses(memberAddresses)
                    .build();
        }
    }
}
