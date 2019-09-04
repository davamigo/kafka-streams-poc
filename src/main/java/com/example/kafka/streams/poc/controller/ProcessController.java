package com.example.kafka.streams.poc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Process controller.
 * Base route: /process
 */
@Controller
@RequestMapping("/process")
public class ProcessController {

    /** Logger object */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessController.class);

    /**
     * GET /process/commercial-order-converter/details
     *
     * Shows the details the commercial order converter process
     *
     * @return the model and view
     */
    @GetMapping("/commercial-order-converter/details")
    public ModelAndView getCommercialOrderConverterProcessDetailsAction() {
        LOGGER.info("ProcessController.getCommercialOrderConverterProcessDetailsAction()");
        return new ModelAndView("process/commercial-order-converter-details");
    }

    /**
     * GET /process/commercial-order-lines-split/details
     *
     * Shows the details the commercial order lines split process
     *
     * @return the model and view
     */
    @GetMapping("/commercial-order-lines-split/details")
    public ModelAndView getCommercialOrderLinesSplitProcessDetailsAction() {
        LOGGER.info("ProcessController.getCommercialOrderLinesSplitProcessDetailsAction()");
        return new ModelAndView("process/commercial-order-lines-split-details");
    }

    /**
     * GET /process/purchase-order-generator/details
     *
     * Shows the details the purchase order generator process
     *
     * @return the model and view
     */
    @GetMapping("/purchase-order-generator/details")
    public ModelAndView getPurchaseOrderGeneratorProcessDetailsAction() {
        LOGGER.info("ProcessController.getPurchaseOrderGeneratorProcessDetailsAction()");
        return new ModelAndView("process/purchase-order-generator-details");
    }

    /**
     * GET /process/purchase-order-line-generator/details
     *
     * Shows the details the purchase order line aggregation process
     *
     * @return the model and view
     */
    @GetMapping("/purchase-order-line-generator/details")
    public ModelAndView getPurchaseOrderLineGeneratorProcessDetailsAction() {
        LOGGER.info("ProcessController.getPurchaseOrderLineGeneratorProcessDetailsAction()");
        return new ModelAndView("process/purchase-order-line-generator-details");
    }

    /**
     * GET /process/warehouse-order-generator/details
     *
     * Shows the details the warehouse order generator process
     *
     * @return the model and view
     */
    @GetMapping("/warehouse-order-generator/details")
    public ModelAndView getWarehouseOrderGeneratorProcessDetailsAction() {
        LOGGER.info("ProcessController.getWarehouseOrderGeneratorProcessDetailsAction()");
        return new ModelAndView("process/warehouse-order-generator-details");
    }

    /**
     * GET /process/warehouse-order-line-generator/details
     *
     * Shows the details the warehouse order line generator process
     *
     * @return the model and view
     */
    @GetMapping("/warehouse-order-line-generator/details")
    public ModelAndView getWarehouseOrderLineGeneratorProcessDetailsAction() {
        LOGGER.info("ProcessController.getWarehouseOrderLineGeneratorProcessDetailsAction()");
        return new ModelAndView("process/warehouse-order-line-generator-details");
    }

    /**
     * GET /process/warehouse-order-line-matcher/details
     *
     * Shows the details the warehouse order line matching with legacy product process
     *
     * @return the model and view
     */
    @GetMapping("/warehouse-order-line-matcher/details")
    public ModelAndView getWarehouseOrderLineMatcherProcessDetailsAction() {
        LOGGER.info("ProcessController.getWarehouseOrderLineMatcherProcessDetailsAction()");
        return new ModelAndView("process/warehouse-order-line-matcher-details");
    }

    /**
     * GET /process/warehouse-order-line-recover/details
     *
     * Shows the details the warehouse order line recover process
     *
     * @return the model and view
     */
    @GetMapping("/warehouse-order-line-recover/details")
    public ModelAndView getWarehouseOrderLineRecoverProcessDetailsAction() {
        LOGGER.info("ProcessController.getWarehouseOrderLineRecoverProcessDetailsAction()");
        return new ModelAndView("process/warehouse-order-line-recover-details");
    }

    /**
     * GET /process/warehouse-order-line-merger/details
     *
     * Shows the details the warehouse order line merger process
     *
     * @return the model and view
     */
    @GetMapping("/warehouse-order-line-merger/details")
    public ModelAndView getWarehouseOrderLineMergerProcessDetailsAction() {
        LOGGER.info("ProcessController.getWarehouseOrderLineMergerProcessDetailsAction()");
        return new ModelAndView("process/warehouse-order-line-merger-details");
    }

    /**
     * GET /process/product-legacy-id-feeder/details
     *
     * Shows the details of the legacy product feeder process
     *
     * @return the model and view
     */
    @GetMapping("/product-legacy-id-feeder/details")
    public ModelAndView getProductLegacyIdFeederProcessDetailsAction() {
        LOGGER.info("ProcessController.getProductLegacyIdFeederProcessDetailsAction()");
        return new ModelAndView("process/product-legacy-id-feeder-details");
    }
}
