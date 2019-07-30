package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderConvertedEntity;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderEntity;
import com.example.kafka.streams.poc.mongodb.entity.CommercialOrderLineSplitEntity;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderConvertedRepository;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderLineSplitRepository;
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

    /** The mongoDB repository where to retrieve the split commercial order lines */
    private final CommercialOrderLineSplitRepository splitCommercialOrderLineRepository;

    /**
     * Autowired constructor
     *
     * @param randomCommercialOrderProducer      the service to produce commercial orders
     * @param newCommercialOrderRepository       the mongoDB new commercial order repository
     * @param convertedCommercialOrderRepository the mongoDB converted commercial order repository
     * @param splitCommercialOrderLineRepository the mongoDB split commercial order line repository
     */
    @Autowired
    public CommercialOrderController(
            RandomCommercialOrderProducer randomCommercialOrderProducer,
            CommercialOrderRepository newCommercialOrderRepository,
            CommercialOrderConvertedRepository convertedCommercialOrderRepository,
            CommercialOrderLineSplitRepository splitCommercialOrderLineRepository
    ) {
        this.randomCommercialOrderProducer = randomCommercialOrderProducer;
        this.newCommercialOrderRepository = newCommercialOrderRepository;
        this.convertedCommercialOrderRepository = convertedCommercialOrderRepository;
        this.splitCommercialOrderLineRepository = splitCommercialOrderLineRepository;
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
     * GET /commercial-order/new-orders
     *
     * Lists the new commercial orders
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/new-orders")
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
     * GET /commercial-order/new-orders/{id}
     *
     * Shows a new commercial order
     *
     * @param uuid the uuid of the commercial order
     * @return the model and view
     */
    @GetMapping("/new-orders/{id}")
    public ModelAndView getNewOrdersAction(@PathVariable("id") String uuid) {

        final Optional<CommercialOrderEntity> commercialOrder = newCommercialOrderRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("commercial-order/show-new");
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

    /**
     * GET /commercial-order/converted/{id}
     *
     * Shows a converted commercial order
     *
     * @param uuid the uuid of the commercial order
     * @return the model and view
     */
    @GetMapping("/converted/{id}")
    public ModelAndView getConvertedOrdersAction(@PathVariable("id") String uuid) {

        final Optional<CommercialOrderConvertedEntity> commercialOrder = convertedCommercialOrderRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("commercial-order/show-converted");
        mav.addObject("uuid", uuid);
        mav.addObject("commercialOrder", commercialOrder.orElse(null));
        return mav;
    }

    /**
     * GET /commercial-order/converted/details
     *
     * Shows the details the commercial order converter process
     *
     * @return the model and view
     */
    @GetMapping("/converted/details")
    public ModelAndView getConvertedOrdersDetailsAction() {
        return new ModelAndView("commercial-order/details-converted");
    }

    /**
     * GET /commercial-order/lines-split
     *
     * Lists the split commercial order liness
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/lines-split")
    public ModelAndView getSplitOrderLinessAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        final List<CommercialOrderLineSplitEntity> lines = splitCommercialOrderLineRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "datetime")))
                .getContent();

        final long count = splitCommercialOrderLineRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("commercial-order/list-lines-split");
        mav.addObject("lines", lines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /commercial-order/lines-split/{id}
     *
     * Shows a split commercial order line
     *
     * @param uuid the uuid of the commercial order line
     * @return the model and view
     */
    @GetMapping("/lines-split/{id}")
    public ModelAndView getSplitOrderLinessAction(@PathVariable("id") String uuid) {

        final Optional<CommercialOrderLineSplitEntity> line = splitCommercialOrderLineRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("commercial-order/show-lines-split");
        mav.addObject("uuid", uuid);
        mav.addObject("line", line.orElse(null));
        return mav;
    }

    /**
     * GET /commercial-order/lines-split/details
     *
     * Shows the details the commercial order lines split process
     *
     * @return the model and view
     */
    @GetMapping("/lines-split/details")
    public ModelAndView getOrderLinesDetailsAction() {
        return new ModelAndView("commercial-order/details-lines-split");
    }
}
