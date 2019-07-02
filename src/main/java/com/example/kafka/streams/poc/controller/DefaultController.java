package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderRepository;
import com.example.kafka.streams.poc.mongodb.repository.MemberRepository;
import com.example.kafka.streams.poc.mongodb.repository.ProductRepository;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Default controller.
 * Base route: /
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    /** The mongoDB repository where to retrieve the commercial orders */
    private final CommercialOrderRepository commercialOrderRepository;

    /** The mongoDB repository where to retrieve the products */
    private final ProductRepository productRepository;

    /** The mongoDB repository where to retrieve the members */
    private final MemberRepository memberRepository;

    /** The mongoDB repository where to retrieve the purchase orders */
    private final PurchaseOrderRepository purchaseOrderRepository;

    /**
     * Autowired constructor
     *
     * @param commercialOrderRepository the mongoDB commercial order repository
     * @param productRepository the mongoDB product repository
     * @param memberRepository the mongoDB member repository
     * @param purchaseOrderRepository the mongoDB purchase order repository
     */
    @Autowired
    public DefaultController(
            CommercialOrderRepository commercialOrderRepository,
            ProductRepository productRepository,
            MemberRepository memberRepository,
            PurchaseOrderRepository purchaseOrderRepository
    ) {
        this.commercialOrderRepository = commercialOrderRepository;
        this.productRepository= productRepository;
        this.memberRepository = memberRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * GET /
     *
     * Default action - shows the homepage.
     *
     * @return the model and view for the template homepage.html
     */
    @GetMapping("/")
    public ModelAndView homepage() {

        final ModelAndView mav  = new ModelAndView("default/homepage");
        mav.addObject("commercialOrderCount", commercialOrderRepository.count());
        mav.addObject("productCount", productRepository.count());
        mav.addObject("memberCount", memberRepository.count());
        mav.addObject("purchaseOrderCount", purchaseOrderRepository.count());
        return mav;
    }
}
