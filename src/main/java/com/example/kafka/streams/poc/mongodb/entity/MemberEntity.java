package com.example.kafka.streams.poc.mongodb.entity;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Member MongoDB entity
 */
@Document(collection="member")
public class MemberEntity {

    /** The unique identifier of the member */
    @Id
    private String uuid;

    /** The first name of the member */
    private String firstName;

    /** The last name of the member */
    private String lastName;

    /** The addresses of the member */
    private List<AddressEntity> addresses;

    /**
     * Empty constructor
     */
    public MemberEntity() {
        this.addresses = new ArrayList<>();
    }

    /**
     * Copy constructor from domain entity
     *
     * @param source the source member object
     */
    public MemberEntity(Member source) {
        this.uuid = source.getUuid();
        this.firstName = source.getFirstName();
        this.lastName = source.getLastName();
        this.addresses = new ArrayList<>();
        for (Address address : source.getAddresses()) {
            this.addresses.add(new AddressEntity(address));
        }
    }

    /**
     * @return the unique identifier of the member
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the first name of the member
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the last name of the member
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the addresses of the member
     */
    public List<AddressEntity> getAddresses() {
        return addresses;
    }
}
