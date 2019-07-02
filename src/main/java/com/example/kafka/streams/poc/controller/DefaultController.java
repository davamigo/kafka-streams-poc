package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.kafka.monitor.BeanNotFoundException;
import com.example.kafka.streams.poc.kafka.monitor.KafkaStreamProcessesStatusMonitor;
import com.example.kafka.streams.poc.mongodb.repository.CommercialOrderRepository;
import com.example.kafka.streams.poc.mongodb.repository.MemberRepository;
import com.example.kafka.streams.poc.mongodb.repository.ProductRepository;
import com.example.kafka.streams.poc.mongodb.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Default controller.
 * Base route: /
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    /** The mongoDB repository where to retrieve the commercial orders */
    private final CommercialOrderRepository commercialOrderRepository;

    /** The mongoDB repository where to retrieve the products */
    private final ProductRepository productRepository;

    /** The mongoDB repository where to retrieve the members */
    private final MemberRepository memberRepository;

    /** The mongoDB repository where to retrieve the purchase orders */
    private final PurchaseOrderRepository purchaseOrderRepository;

    /** The status monitor for the Kafka Stream processes */
    private final KafkaStreamProcessesStatusMonitor kafkaStreamProcessesStatusMonitor;

    /**
     * Autowired constructor
     *
     * @param commercialOrderRepository the mongoDB commercial order repository
     * @param productRepository the mongoDB product repository
     * @param memberRepository the mongoDB member repository
     * @param purchaseOrderRepository the mongoDB purchase order repository
     * @param kafkaStreamProcessesStatusMonitor the status monitor for the Kafka Stream processes
     */
    @Autowired
    public DefaultController(
            CommercialOrderRepository commercialOrderRepository,
            ProductRepository productRepository,
            MemberRepository memberRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            KafkaStreamProcessesStatusMonitor kafkaStreamProcessesStatusMonitor
    ) {
        this.commercialOrderRepository = commercialOrderRepository;
        this.productRepository= productRepository;
        this.memberRepository = memberRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.kafkaStreamProcessesStatusMonitor = kafkaStreamProcessesStatusMonitor;
    }

    /**
     * GET /
     *
     * Default action - shows the homepage.
     *
     * @return the model and view for the template homepage.html
     */
    @GetMapping("/")
    public ModelAndView homepage() {

        Map<String, Boolean> processesStatuses = new HashMap<>();
        for (String qualifier : kafkaStreamProcessesStatusMonitor.getBeanQualifiers()) {
            boolean status = false;
            try {
                status = kafkaStreamProcessesStatusMonitor.isRunning(qualifier);
            } catch (BeanNotFoundException exc) {
                exc.printStackTrace();
            }
            processesStatuses.put(qualifier, status);
        }

        final ModelAndView mav  = new ModelAndView("default/homepage");
        mav.addObject("commercialOrderCount", commercialOrderRepository.count());
        mav.addObject("productCount", productRepository.count());
        mav.addObject("memberCount", memberRepository.count());
        mav.addObject("purchaseOrderCount", purchaseOrderRepository.count());
        mav.addObject("processesStatuses", processesStatuses);
        return mav;
    }

    @PostMapping("/process/{action}")
    public ModelAndView startStopKafkaStreamsAction(
            @PathVariable("action") String action
    ) throws Exception {

        switch (action) {
            case "start":
                kafkaStreamProcessesStatusMonitor.startAll();
                break;

            case "stop":
                kafkaStreamProcessesStatusMonitor.stopAll();
                break;

            default:
                throw new Exception("DefaultController.startStopKafkaStreamsAction: Action " + action + " not valid");
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/process/{procid}/{action}")
    public ModelAndView startStopKafkaStreamsAction(
            @PathVariable("procid") String procid,
            @PathVariable("action") String action
    ) throws Exception {

        switch (action) {
            case "start":
                kafkaStreamProcessesStatusMonitor.start(procid);
                break;

            case "stop":
                kafkaStreamProcessesStatusMonitor.stop(procid);
                break;

            default:
                throw new Exception("DefaultController.startStopKafkaStreamsAction: Action " + action + " not valid");
        }

        return new ModelAndView("redirect:/");
    }
}
