package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.mongodb.entity.PurchaseOrderEntity;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

/**
 * Purchase order controller.
 * Base route: /purchase-order
 */
@Controller
@RequestMapping("/purchase-order")
public class PurchaseOrderController {

    /** The mongoDB repository where to retrieve the purchase orders */
    private PurchaseOrderRepository purchaseOrderRepository;

    /**
     * Autowired constructor
     *
     * @param purchaseOrderRepository the mongoDB purchase order repository
     */
    @Autowired
    public PurchaseOrderController(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    /**
     * GET /purchase-order
     *
     * Lists the purchase orders
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping({"", "/"})
    public ModelAndView getOrdersAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        ModelAndView mav  = new ModelAndView("purchase-order/list");

        List<PurchaseOrderEntity> purchaseOrders = purchaseOrderRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "datetime")))
                .getContent();

        long count = purchaseOrderRepository.count();
        long prev = (page > 0) ? page - 1 : 0;
        long next = (size * (page + 1) < count) ? page + 1 : page;

        mav.addObject("purchaseOrders", purchaseOrders);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);

        return mav;
    }

    /**
     * GET /purchase-order/{id}
     *
     * Shows a purchase order
     *
     * @param uuid the uuid of the purchase order
     * @return the model and view
     */
    @GetMapping("/{id}")
    public ModelAndView getOrdersAction(@PathVariable("id") String uuid) {
        ModelAndView mav  = new ModelAndView("purchase-order/show");

        Optional<PurchaseOrderEntity> purchaseOrder = purchaseOrderRepository.findById(uuid);

        mav.addObject("uuid", uuid);
        mav.addObject("purchaseOrder", purchaseOrder.orElse(null));

        return mav;
    }
}
