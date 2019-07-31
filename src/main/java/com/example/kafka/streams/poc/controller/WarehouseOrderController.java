package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineFailedEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineFailedRepository;
import com.example.kafka.streams.poc.service.producer.warehouseorder.ManuallyRecoveredWarehouseOrderLineProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Warehouse order controller.
 * Base route: /warehouse-order
 */
@Controller
@RequestMapping("/warehouse-order")
public class WarehouseOrderController {

    /** The mongoDB repository where to retrieve the failed warehouse order lines */
    private final WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository;

    /** The service to publish the recovered warehouse order line */
    private final ManuallyRecoveredWarehouseOrderLineProducer warehouseOrderLineProducer;

    /**
     * Autowired constructor
     *
     * @param warehouseOrderLineFailedRepository the mongoDB warehouse order line failed repository
     * @param warehouseOrderLineProducer         the service to publish the recovered warehouse order line
     */
    @Autowired
    public WarehouseOrderController(
            WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository,
            ManuallyRecoveredWarehouseOrderLineProducer warehouseOrderLineProducer
    ) {
        this.warehouseOrderLineFailedRepository = warehouseOrderLineFailedRepository;
        this.warehouseOrderLineProducer = warehouseOrderLineProducer;
    }

    /**
     * GET /warehouse-order/line/details
     *
     * Shows the details the warehouse order lines generation process
     *
     * @return the model and view
     */
    @GetMapping("/line/details")
    public ModelAndView getOrderLinessDetailsAction() {
        return new ModelAndView("warehouse-order/details");
    }

    /**
     * GET /warehouse-order/line/match/details
     *
     * Shows the details the warehouse order lines matching with legacy product process
     *
     * @return the model and view
     */
    @GetMapping("/line/match/details")
    public ModelAndView getOrderLineMatchsDetailsAction() {
        return new ModelAndView("warehouse-order/details-match");
    }

    /**
     * GET /warehouse-order/line/failed
     *
     * Lists the failed warehouse order liness
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/line/failed")
    public ModelAndView getFailedWarehouseOrderLinesAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        final List<WarehouseOrderLineFailedEntity> orderLines = warehouseOrderLineFailedRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderLineFailedRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-failed-lines");
        mav.addObject("orderLines", orderLines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/line/failed/{id}
     *
     * Shows a failed warehouse order line
     *
     * @param uuid the uuid of the warehouse order line
     * @return the model and view
     */
    @GetMapping("/line/failed/{id}")
    public ModelAndView getOrdersAction(@PathVariable("id") String uuid) {

        final Optional<WarehouseOrderLineFailedEntity> warehouseOrderLine = warehouseOrderLineFailedRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-failed-line");
        mav.addObject("uuid", uuid);
        mav.addObject("warehouseOrderLine", warehouseOrderLine.orElse(null));
        return mav;
    }

    /**
     * POST /warehouse-order/line/failed/{id}
     *
     * Updates the product legacy id of the failed warehouse order line
     *
     * @param uuid            the uuid of the warehouse order line
     * @param productLegacyId the new product legacy id
     * @return the model and view
     */
    @PostMapping("/line/failed/{id}")
    public ModelAndView postCreateOrdersAction(
            @PathVariable("id") String uuid,
            @RequestParam Integer productLegacyId
    ) {
        // Read the warehouse order line from the MongoDB database
        WarehouseOrderLineFailedEntity entity = warehouseOrderLineFailedRepository.findById(uuid).orElse(null);
        if (entity == null) {
            return new ModelAndView("redirect:/warehouse-order/line/failed/{id}", Collections.singletonMap("id", uuid));
        }

        // Publish the warehouse order line in the `recovered` topic
        warehouseOrderLineProducer.publish(WarehouseOrderLine.newBuilder().set(entity).setProductLegacyId(productLegacyId).build());

        // Delete the warehouse order line from mongo
        warehouseOrderLineFailedRepository.delete(entity);

        // Redirect to the failed warehouse order lines controller
        return new ModelAndView("redirect:/warehouse-order/line/failed");
    }
}
