package com.example.kafka.streams.poc.service.generator.address;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Service to get an address for generating testing data
 */
@Component
public class RandomAddressGenerator implements AddressGeneratorInterface {

    /** Array of addresses */
    private static final String[][] addresses = {
            {"US", "Arizona",       "Tucson",        "85718", "Parkway Drive",    "2926", null},
            {"US", "California",    "Sacramento",    "95814", "Francis Mine",     "1478", null},
            {"US", "Georgia",       "Atlanta",       "30303", "Musgrave Street",  "3078", null},
            {"US", "Florida",       "Brooker",       "32622", "George Street",    "3743", null},
            {"US", "Florida",       "Port St Lucie", "33452", "Elkview Drive",    "1526", null},
            {"US", "Kentucky",      "Fairplay",      "42735", "Glen Street",      "2841", null},
            {"US", "Massachusetts", "Springfield",   "01103", "Trouser Leg Road", "4003", null},
            {"US", "Michigan",      "Grand Rapids",  "49503", "Post Avenue",      "3465", null},
            {"US", "Minnesota",     "Nashwauk",      "55769", "Cottonwood Lane",  "338",  null},
            {"US", "Pennsylvania",  "Harrisburg",    "15222", "Jacobs Street",    "2173", null},
            {"US", "Pennsylvania",  "Pittsburgh",    "17101", "Lincoln Drive",    "3455", null},
            {"US", "Tennessee",     "Memphis",       "38110", "Edgewood Road",    "133",  null},
            {"US", "Texas",         "Dallas",        "75204", "Whitetail Lane",   "3795", null},
            {"US", "Utah",          "Mountain View", "82939", "Lang Avenue",      "1637", null},
            {"US", "Wisconsin",     "Milwaukee",     "53211", "Larry Street",     "4541", null},
    };

    /**
     * Default constructor
     */
    public RandomAddressGenerator() {
    }

    /**
     * Get a random address from the list
     *
     * @return a random address
     */
    @Override
    public synchronized Address getAddress() {

        int index = (new Random()).nextInt(addresses.length);
        Address.Builder builder = Address.newBuilder()
                .setCountry(addresses[index][0])
                .setState(addresses[index][1])
                .setCity(addresses[index][2])
                .setZipCode(addresses[index][3])
                .setStreet(addresses[index][4])
                .setNumber(addresses[index][5])
                .setExtra(addresses[index][6]);

        return builder.build();
    }
}
