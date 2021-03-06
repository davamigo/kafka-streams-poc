package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.mongodb.entity.ProductEntity;
import com.example.kafka.streams.poc.mongodb.entity.ProductLegacyIdEntity;
import com.example.kafka.streams.poc.mongodb.repository.ProductLegacyIdRepository;
import com.example.kafka.streams.poc.mongodb.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Product controller.
 * Base route: /product
 */
@Controller
@RequestMapping("/product")
public class ProductController {

    /** Logger object */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    /** The mongoDB repository where to retrieve the products */
    private final ProductRepository productRepository;

    /** The mongoDB repository where to retrieve the legacy products */
    private final ProductLegacyIdRepository productLegacyIdRepository;

    /**
     * Autowired constructor
     *
     * @param productRepository         the mongoDB product repository
     * @param productLegacyIdRepository the mongoDB legacy product repository
     */
    @Autowired
    public ProductController(ProductRepository productRepository, ProductLegacyIdRepository productLegacyIdRepository) {
        this.productRepository = productRepository;
        this.productLegacyIdRepository = productLegacyIdRepository;
    }

    /**
     * GET /product
     *
     * Lists the products
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping({"", "/"})
    public ModelAndView getProductsAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("ProductController.getProductsAction(size=" + size + ", page=" + page + ")");

        final List<ProductEntity> products = productRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, Arrays.asList("firstName", "lastName"))))
                .getContent();

        final long count = productRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("product/list");
        mav.addObject("products", products);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }

    /**
     * GET /product/{id}
     *
     * Shows a product
     *
     * @param uuid the uuid of the product
     * @return the model and view
     */
    @GetMapping("/{id}")
    public ModelAndView getProductAction(@PathVariable("id") String uuid) {
        LOGGER.info("ProductController.getProductAction(id=" + uuid + ")");

        final Optional<ProductEntity> product = productRepository.findById(uuid);

        final ModelAndView mav  = new ModelAndView("product/show");
        mav.addObject("uuid", uuid);
        mav.addObject("product", product.orElse(null));
        return mav;
    }

    /**
     * GET /product/legacy-id
     *
     * Lists the legacy ids for the  products
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping("/legacy-id")
    public ModelAndView getLegacyProductsAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        LOGGER.info("ProductController.getLegacyProductsAction(size=" + size + ", page=" + page + ")");

        final List<ProductLegacyIdEntity> products = productLegacyIdRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, Arrays.asList("firstName", "lastName"))))
                .getContent();

        final long count = productLegacyIdRepository.count();
        final long prev = (page > 0) ? page - 1 : 0;
        final long next = (size * (page + 1) < count) ? page + 1 : page;

        final ModelAndView mav  = new ModelAndView("product/list-legacy-ids");
        mav.addObject("products", products);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);
        return mav;
    }
}
