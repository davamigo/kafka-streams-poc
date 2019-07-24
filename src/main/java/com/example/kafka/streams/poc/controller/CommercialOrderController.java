package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderConvertedEntity;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderConvertedRepository;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderRepository;
import com.example.kafka.streams.poc.service.producer.commercialorder.RandomCommercialOrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

/**
 * Commercial order controller.
 * Base route: /commercial-order
 */
@Controller
@RequestMapping("/commercial-order")
public class CommercialOrderController {

    /** Service to produce one or more commercial order with random data */
    private final RandomCommercialOrderProducer randomCommercialOrderProducer;

    /** The mongoDB repository where to retrieve the new commercial orders */
    private final CommercialOrderRepository newCommercialOrderRepository;

    /** The mongoDB repository where to retrieve the converted commercial orders */
    private final CommercialOrderConvertedRepository convertedCommercialOrderRepository;

    /**
     * Autowired constructor
     *
     * @param randomCommercialOrderProducer service to produce commercial orders
     * @param newCommercialOrderRepository the mongoDB new commercial order repository
     * @param convertedCommercialOrderRepository the mongoDB converted commercial order repository
     */
    @Autowired
    public CommercialOrderController(
            RandomCommercialOrderProducer randomCommercialOrderProducer,
            CommercialOrderRepository newCommercialOrderRepository,
            CommercialOrderConvertedRepository convertedCommercialOrderRepository
    ) {
        this.randomCommercialOrderProducer = randomCommercialOrderProducer;
        this.newCommercialOrderRepository = newCommercialOrderRepository;
        this.convertedCommercialOrderRepository = convertedCommercialOrderRepository;
    }

    /**
     * POST /commercial-order/create
     *
     * Creates one of more commercial orders with random data
     *
     * @param orderCount the number of commercial orders to create
     * @return the model and view
     */
    @PostMapping("/create")
    public ModelAndView postCreateOrdersAction(@RequestParam Integer orderCount) {
        final ModelAndView mav  = new ModelAndView("commercial-order/created");
        mav.addObject("orderCount", orderCount);

        try {
            List<CommercialOrder> commercialOrders = randomCommercialOrderProducer.produce(orderCount);
            mav.addObject("commercialOrders", commercialOrders);
        }
        catch (Exception exc) {
            exc.printStackTrace();
            mav.addObject("errorText", exc.getMessage());
        }

        return mav;
    }

    /**
     * GET /commercial-order/new
     *
     * Lists the commercial orders
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/new")
    public ModelAndView getNewOrdersAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        final List<CommercialOrderEntity> commercialOrders = newCommercialOrderRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "datetime")))
                .getContent();

        final long count = newCommercialOrderRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("commercial-order/list-new");
        mav.addObject("commercialOrders", commercialOrders);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /commercial-order/new/{id}
     *
     * Shows a commercial order
     *
     * @param uuid the uuid of the commercial order
     * @return the model and view
     */
    @GetMapping("/new/{id}")
    public ModelAndView getOrdersAction(@PathVariable("id") String uuid) {

        final Optional<CommercialOrderEntity> commercialOrder = newCommercialOrderRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("commercial-order/show");
        mav.addObject("uuid", uuid);
        mav.addObject("commercialOrder", commercialOrder.orElse(null));
        return mav;
    }

    /**
     * GET /commercial-order/converted
     *
     * Lists the converted commercial orders
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/converted")
    public ModelAndView getConvertedOrdersAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        final List<CommercialOrderConvertedEntity> orders = convertedCommercialOrderRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "datetime")))
                .getContent();

        final long count = convertedCommercialOrderRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("commercial-order/list-converted");
        mav.addObject("commercialOrders", orders);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }
}
