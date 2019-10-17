package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.warehouse.WarehouseOrderLine;
import com.example.kafka.streams.poc.mongodb.entity.*;
import com.example.kafka.streams.poc.mongodb.repository.*;
import com.example.kafka.streams.poc.service.producer.warehouseorder.ManuallyRecoveredWarehouseOrderLineProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /** Logger object */
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseOrderController.class);

    /** The mongoDB repository where to retrieve the generated warehouse order lines */
    private final WarehouseOrderLineRepository warehouseOrderLineGeneratedRepository;

    /** The mongoDB repository where to retrieve the matched warehouse order lines */
    private final WarehouseOrderLineMatchedRepository warehouseOrderLineMatchedRepository;

    /** The mongoDB repository where to retrieve the unmatched warehouse order lines */
    private final WarehouseOrderLineUnmatchedRepository warehouseOrderLineUnmatchedRepository;

    /** The mongoDB repository where to retrieve the recovered warehouse order lines */
    private final WarehouseOrderLineRecoveredRepository warehouseOrderLineRecoveredRepository;

    /** The mongoDB repository where to retrieve the failed warehouse order lines */
    private final WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository;

    /** The mongoDB repository where to retrieve the merged warehouse order lines */
    private final WarehouseOrderLineMergedRepository warehouseOrderLineMergedRepository;

    /** The mongoDB repository where to retrieve the warehouse orders */
    private final WarehouseOrderRepository warehouseOrderRepository;

    /** The service to publish the recovered warehouse order line */
    private final ManuallyRecoveredWarehouseOrderLineProducer warehouseOrderLineProducer;

    /**
     * Autowired constructor
     *
     * @param warehouseOrderLineGeneratedRepository the mongoDB warehouse order line generated repository
     * @param warehouseOrderLineMatchedRepository   the mongoDB warehouse order line matched repository
     * @param warehouseOrderLineUnmatchedRepository the mongoDB warehouse order line unmatched repository
     * @param warehouseOrderLineRecoveredRepository the mongoDB warehouse order line recovered repository
     * @param warehouseOrderLineFailedRepository    the mongoDB warehouse order line failed repository
     * @param warehouseOrderLineMergedRepository    the mongoDB warehouse order line merged repository
     * @param warehouseOrderRepository              the mongoDB warehouse order repository
     * @param warehouseOrderLineProducer            the service to publish the recovered warehouse order line
     */
    @Autowired
    public WarehouseOrderController(
            WarehouseOrderLineRepository warehouseOrderLineGeneratedRepository,
            WarehouseOrderLineMatchedRepository warehouseOrderLineMatchedRepository,
            WarehouseOrderLineUnmatchedRepository warehouseOrderLineUnmatchedRepository,
            WarehouseOrderLineRecoveredRepository warehouseOrderLineRecoveredRepository,
            WarehouseOrderLineFailedRepository warehouseOrderLineFailedRepository,
            WarehouseOrderLineMergedRepository warehouseOrderLineMergedRepository,
            ManuallyRecoveredWarehouseOrderLineProducer warehouseOrderLineProducer,
            WarehouseOrderRepository warehouseOrderRepository
    ) {
        this.warehouseOrderLineGeneratedRepository = warehouseOrderLineGeneratedRepository;
        this.warehouseOrderLineMatchedRepository = warehouseOrderLineMatchedRepository;
        this.warehouseOrderLineUnmatchedRepository = warehouseOrderLineUnmatchedRepository;
        this.warehouseOrderLineRecoveredRepository = warehouseOrderLineRecoveredRepository;
        this.warehouseOrderLineFailedRepository = warehouseOrderLineFailedRepository;
        this.warehouseOrderLineMergedRepository = warehouseOrderLineMergedRepository;
        this.warehouseOrderRepository = warehouseOrderRepository;
        this.warehouseOrderLineProducer = warehouseOrderLineProducer;
    }

    /**
     * GET /warehouse-order/line/generated
     *
     * Lists the generated warehouse order lines
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/line/generated")
    public ModelAndView getGeneratedWarehouseOrderLinesAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("WarehouseOrderController.getGeneratedWarehouseOrderLinesAction(size=" + size + ", page=" + page + ")");

        final List<WarehouseOrderLineEntity> orderLines = warehouseOrderLineGeneratedRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderLineGeneratedRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-generated-lines");
        mav.addObject("orderLines", orderLines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/line/generated/{id}
     *
     * Shows a generated warehouse order line
     *
     * @param uuid the uuid of the warehouse order line
     * @return the model and view
     */
    @GetMapping("/line/generated/{id}")
    public ModelAndView getGeneratedWarehouseOrderLineAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getGeneratedWarehouseOrderLineAction(id=" + uuid + ")");

        final Optional<WarehouseOrderLineEntity> warehouseOrderLine = warehouseOrderLineGeneratedRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-generated-line");
        mav.addObject("uuid", uuid);
        mav.addObject("warehouseOrderLine", warehouseOrderLine.orElse(null));
        return mav;
    }

    /**
     * GET /warehouse-order/line/matched
     *
     * Lists the matched warehouse order lines
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/line/matched")
    public ModelAndView getMatchedWarehouseOrderLinesAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("WarehouseOrderController.getMatchedWarehouseOrderLinesAction(size=" + size + ", page=" + page + ")");

        final List<WarehouseOrderLineMatchedEntity> orderLines = warehouseOrderLineMatchedRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderLineMatchedRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-matched-lines");
        mav.addObject("orderLines", orderLines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/line/matched/{id}
     *
     * Shows a matched warehouse order line
     *
     * @param uuid the uuid of the warehouse order line
     * @return the model and view
     */
    @GetMapping("/line/matched/{id}")
    public ModelAndView getMatchedWarehouseOrderLineAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getMatchedWarehouseOrderLineAction(id=" + uuid + ")");

        final Optional<WarehouseOrderLineMatchedEntity> warehouseOrderLine = warehouseOrderLineMatchedRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-matched-line");
        mav.addObject("uuid", uuid);
        mav.addObject("warehouseOrderLine", warehouseOrderLine.orElse(null));
        return mav;
    }

    /**
     * GET /warehouse-order/line/unmatched
     *
     * Lists the unmatched warehouse order lines
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/line/unmatched")
    public ModelAndView getUnmatchedWarehouseOrderLinesAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("WarehouseOrderController.getUnmatchedWarehouseOrderLinesAction(size=" + size + ", page=" + page + ")");

        final List<WarehouseOrderLineUnmatchedEntity> orderLines = warehouseOrderLineUnmatchedRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderLineUnmatchedRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-unmatched-lines");
        mav.addObject("orderLines", orderLines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/line/unmatched/{id}
     *
     * Shows a unmatched warehouse order line
     *
     * @param uuid the uuid of the warehouse order line
     * @return the model and view
     */
    @GetMapping("/line/unmatched/{id}")
    public ModelAndView getUnmatchedWarehouseOrderLineAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getUnmatchedWarehouseOrderLineAction(id=" + uuid + ")");

        final Optional<WarehouseOrderLineUnmatchedEntity> warehouseOrderLine = warehouseOrderLineUnmatchedRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-unmatched-line");
        mav.addObject("uuid", uuid);
        mav.addObject("warehouseOrderLine", warehouseOrderLine.orElse(null));
        return mav;
    }

    /**
     * GET /warehouse-order/line/recovered
     *
     * Lists the recovered warehouse order lines
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/line/recovered")
    public ModelAndView getRecoveredWarehouseOrderLinesAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("WarehouseOrderController.getRecoveredWarehouseOrderLinesAction(size=" + size + ", page=" + page + ")");

        final List<WarehouseOrderLineRecoveredEntity> orderLines = warehouseOrderLineRecoveredRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderLineRecoveredRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-recovered-lines");
        mav.addObject("orderLines", orderLines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/line/recovered/{id}
     *
     * Shows a recovered warehouse order line
     *
     * @param uuid the uuid of the warehouse order line
     * @return the model and view
     */
    @GetMapping("/line/recovered/{id}")
    public ModelAndView getRecoveredWarehouseOrderLineAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getRecoveredWarehouseOrderLineAction(id=" + uuid + ")");

        final Optional<WarehouseOrderLineRecoveredEntity> warehouseOrderLine = warehouseOrderLineRecoveredRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-recovered-line");
        mav.addObject("uuid", uuid);
        mav.addObject("warehouseOrderLine", warehouseOrderLine.orElse(null));
        return mav;
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
        LOGGER.info("WarehouseOrderController.getFailedWarehouseOrderLinesAction(size=" + size + ", page=" + page + ")");

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
    public ModelAndView getFailedWarehouseOrderLineAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getFailedWarehouseOrderLineAction(id=" + uuid + ")");

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
    public ModelAndView postFailedWarehouseOrderLineAction(
            @PathVariable("id") String uuid,
            @RequestParam Integer productLegacyId
    ) {
        LOGGER.info("WarehouseOrderController.postFailedWarehouseOrderLineAction(id=" + uuid + ", productLegacyId=" + productLegacyId + ")");

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

    /**
     * GET /warehouse-order/line/full
     *
     * Lists the full warehouse order lines
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/line/full")
    public ModelAndView getFullWarehouseOrderLinesAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("WarehouseOrderController.getFullWarehouseOrderLinesAction(size=" + size + ", page=" + page + ")");

        final List<WarehouseOrderLineMergedEntity> orderLines = warehouseOrderLineMergedRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderLineMergedRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-full-lines");
        mav.addObject("orderLines", orderLines);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/line/full/{id}
     *
     * Shows a full warehouse order line
     *
     * @param uuid the uuid of the warehouse order line
     * @return the model and view
     */
    @GetMapping("/line/full/{id}")
    public ModelAndView getFullWarehouseOrderLineAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getFullWarehouseOrderLineAction(id=" + uuid + ")");

        final Optional<WarehouseOrderLineMergedEntity> warehouseOrderLine = warehouseOrderLineMergedRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-full-line");
        mav.addObject("uuid", uuid);
        mav.addObject("warehouseOrderLine", warehouseOrderLine.orElse(null));
        return mav;
    }

    /**
     * GET /warehouse-order
     *
     * Lists the warehouse orders
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping({"", "/"})
    public ModelAndView getWarehouseOrdersAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("WarehouseOrderController.getWarehouseOrdersAction(size=" + size + ", page=" + page + ")");

        final List<WarehouseOrderEntity> orders = warehouseOrderRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "date")))
                .getContent();

        final long count = warehouseOrderRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("warehouse-order/list-orders");
        mav.addObject("orders", orders);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /warehouse-order/{id}
     *
     * Shows a full warehouse order
     *
     * @param uuid the uuid of the warehouse order
     * @return the model and view
     */
    @GetMapping("/{id}")
    public ModelAndView getWarehouseOrderAction(@PathVariable("id") String uuid) {
        LOGGER.info("WarehouseOrderController.getWarehouseOrderAction(id=" + uuid + ")");

        final Optional<WarehouseOrderEntity> order = warehouseOrderRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("warehouse-order/show-order");
        mav.addObject("uuid", uuid);
        mav.addObject("order", order.orElse(null));
        return mav;
    }
}
