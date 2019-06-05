package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineRepository;
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


    /** The mongoDB repository where to retrieve the warehouse order lines */
    private WarehouseOrderLineRepository warehouseOrderLineRepository;

    /** The service to publish the recovered warehouse order line */
    private ManuallyRecoveredWarehouseOrderLineProducer warehouseOrderLineProducer;

    /**
     * Autowired constructor
     *
     * @param warehouseOrderLineRepository the mongoDB warehouse order line repository
     * @param warehouseOrderLineProducer   the service to publish the recovered warehouse order line
     */
    @Autowired
    public WarehouseOrderController(
            WarehouseOrderLineRepository warehouseOrderLineRepository,
            ManuallyRecoveredWarehouseOrderLineProducer warehouseOrderLineProducer
    ) {
        this.warehouseOrderLineRepository = warehouseOrderLineRepository;
        this.warehouseOrderLineProducer = warehouseOrderLineProducer;
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
        ModelAndView mav  = new ModelAndView("warehouse-order/list-failed-lines");

        List<WarehouseOrderLineEntity> orderLines = warehouseOrderLineRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        long count = warehouseOrderLineRepository.count();
        long prev = (page > 0) ? page - 1 : 0;
        long next = (size * (page + 1) < count) ? page + 1 : page;

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
        ModelAndView mav  = new ModelAndView("warehouse-order/show-failed-line");

        Optional<WarehouseOrderLineEntity> warehouseOrderLine = warehouseOrderLineRepository.findById(uuid);

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
        WarehouseOrderLineEntity entity = warehouseOrderLineRepository.findById(uuid).orElse(null);
        if (entity == null) {
            return new ModelAndView("redirect:/warehouse-order/line/failed/{id}", Collections.singletonMap("id", uuid));
        }

        // Publish the warehouse order line in the `recovered` topic
        warehouseOrderLineProducer.publish(WarehouseOrderLine
                .newBuilder()
                .set(entity)
                .setProductLegacyId(productLegacyId)
                .build());

        // Delete the warehouse order line from mongo
        warehouseOrderLineRepository.delete(entity);

        // Redirect to the failed warehouse order lines controller
        return new ModelAndView("redirect:/warehouse-order/line/failed");
    }
}
