package com.example.kafka.streams.poc.service.generator.order;

import com.example.kafka.streams.poc.domain.entity.address.Address;
import com.example.kafka.streams.poc.domain.entity.member.Member;
import com.example.kafka.streams.poc.domain.entity.order.CommercialOrder;
import com.example.kafka.streams.poc.domain.entity.order.CommercialOrderLine;
import com.example.kafka.streams.poc.domain.entity.product.Product;
import com.example.kafka.streams.poc.service.generator.member.MemberGeneratorInterface;
import com.example.kafka.streams.poc.service.generator.member.ReusableMemberSelector;
import com.example.kafka.streams.poc.service.generator.product.ProductGeneratorInterface;
import com.example.kafka.streams.poc.service.generator.product.ReusableProductSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Service to get a commercial order for generating testing data
 */
@Component
public class RandomCommercialOrderGenerator implements CommercialOrderGeneratorInterface {

    /** Service to generate members with random data */
    private MemberGeneratorInterface memberGenerator;

    /** Service to generate products with random data */
    private ProductGeneratorInterface productGenerator;

    /** The price margin (1.3f = 3%) */
    private static float PRICE_MARGIN = 1.3f;

    /**
     * Autowired constructor
     *
     * @param memberGenerator  the service to generate members with random data
     * @param productGenerator the service to generate products with random data
     */
    @Autowired
    public RandomCommercialOrderGenerator(ReusableMemberSelector memberGenerator, ReusableProductSelector productGenerator) {
        this.memberGenerator = memberGenerator;
        this.productGenerator = productGenerator;
    }

    /**
     * Get a commercial order
     *
     * @return a commercial order
     */
    @Override
    public synchronized CommercialOrder getCommercialOrder() {

        String uuid = getRandomUuid();
        Member member = memberGenerator.getMember();
        Address shippingAddress = member.getAddresses().get(0);
        Address billingAddress = null;
        if (member.getAddresses().size() > 1) {
            billingAddress = member.getAddresses().get(1);
        }
        List<CommercialOrderLine> lines = getOrderLines(uuid);

        CommercialOrder.Builder builder = CommercialOrder.newBuilder()
                .setUuid(uuid)
                .setMember(member)
                .setShippingAddress(shippingAddress)
                .setBillingAddress(billingAddress)
                .setLines(lines);


        return builder.build();
    }

    /**
     * @return random UUID
     */
    private String getRandomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param commercialOrderUuid the uuid of the order
     * @return random order lines
     */
    private List<CommercialOrderLine> getOrderLines(String commercialOrderUuid) {

        // Add 1 to 10 order lines
        int num = 1 + ((new Random()).nextInt(10));
        List<CommercialOrderLine> lines = new ArrayList<>();
        while (num-- > 0) {
            lines.add(createRandomOrderLine(commercialOrderUuid));
        }

        return lines;
    }

    /**
     * @param commercialOrderUuid the uuid of the order
     * @return random order line
     */
    private CommercialOrderLine createRandomOrderLine(String commercialOrderUuid) {

        Product product = productGenerator.getProduct();

        CommercialOrderLine.Builder builder = CommercialOrderLine.newBuilder()
                .setUuid(getRandomUuid())
                .setCommercialOrderUuid(commercialOrderUuid)
                .setProduct(product)
                .setPrice(((float) Math.round(100 * product.getPrice() * PRICE_MARGIN)) / 100)
                .setQuantity(1 + ((new Random()).nextInt(5)));

        return builder.build();
    }
}
