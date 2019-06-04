package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.mongodb.entity.WarehouseOrderLineEntity;
import com.example.kafka.streams.poc.mongodb.repository.WarehouseOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * Autowired constructor
     *
     * @param warehouseOrderLineRepository the mongoDB warehouse order line repository
     */
    @Autowired
    public WarehouseOrderController(WarehouseOrderLineRepository warehouseOrderLineRepository) {
        this.warehouseOrderLineRepository = warehouseOrderLineRepository;
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

}