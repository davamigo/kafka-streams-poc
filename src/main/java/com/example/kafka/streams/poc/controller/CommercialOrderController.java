package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.order.CommercialOrder;
import com.example.kafka.streams.poc.usecase.RandomCommercialOrderProducerUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Commercial order controller.
 * Base route: /commercial-order
 */
@Controller
@RequestMapping("/commercial-order")
public class CommercialOrderController {

    /** Use case to produce one or more commercial order with random data */
    private RandomCommercialOrderProducerUseCase commercialOrderProducerUseCase;

    /**
     * Autowired constructor
     *
     * @param commercialOrderProducerUseCase use case
     */
    @Autowired
    public CommercialOrderController(RandomCommercialOrderProducerUseCase commercialOrderProducerUseCase) {
        this.commercialOrderProducerUseCase = commercialOrderProducerUseCase;
    }

    /**
     * GET /commercial-order/create
     *
     * @param orderCount the number of commercial orders to create
     * @return the name of the template to load
     */
    @PostMapping("/create")
    public ModelAndView postCreateAction(@RequestParam Integer orderCount) {
        ModelAndView mav  = new ModelAndView("commercial-order/created");
        mav.addObject("orderCount", orderCount);

        try {
            List<CommercialOrder> commercialOrders = commercialOrderProducerUseCase.run(orderCount);
            mav.addObject("commercialOrders", commercialOrders);
        }
        catch (Exception exc) {
            exc.printStackTrace();
            mav.addObject("errorText", exc.getMessage());
        }

        return mav;
    }
}
