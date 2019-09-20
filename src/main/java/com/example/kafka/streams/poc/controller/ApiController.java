package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.domain.entity.commercialorder.CommercialOrder;
import com.example.kafka.streams.poc.kafka.monitor.BeanNotFoundException;
import com.example.kafka.streams.poc.kafka.monitor.KafkaStreamProcessesStatusMonitor;
import com.example.kafka.streams.poc.mongodb.repository.RecordCountRepository;
import com.example.kafka.streams.poc.service.producer.commercialorder.RandomCommercialOrderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API controller.
 * Base route: /api
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    /** Logger object */
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    /** Service to produce one or more commercial order with random data */
    private final RandomCommercialOrderProducer randomCommercialOrderProducer;

    /** The mongoDB repository where to retrieve the products */
    private final RecordCountRepository recordCountRepository;

    /** The status monitor for the Kafka Stream processes */
    private final KafkaStreamProcessesStatusMonitor kafkaStreamProcessesStatusMonitor;

    /**
     * Autowired constructor
     *
     * @param randomCommercialOrderProducer service to produce commercial orders
     * @param recordCountRepository the mongoDB product repository
     * @param kafkaStreamProcessesStatusMonitor the status monitor for the Kafka Stream processes
     */
    @Autowired
    public ApiController(
            RandomCommercialOrderProducer randomCommercialOrderProducer,
            RecordCountRepository recordCountRepository,
            KafkaStreamProcessesStatusMonitor kafkaStreamProcessesStatusMonitor
    ) {
        this.randomCommercialOrderProducer = randomCommercialOrderProducer;
        this.recordCountRepository = recordCountRepository;
        this.kafkaStreamProcessesStatusMonitor = kafkaStreamProcessesStatusMonitor;
    }

    /**
     * POST /commercial-order/create
     *
     * Creates one of more commercial orders with random data
     *
     * @param orderCount the number of commercial orders to create
     * @return the list of hte comercial orders created
     */
    @PostMapping("/commercial-order/create")
    @ResponseStatus(code = HttpStatus.CREATED)
    public List<CommercialOrder> createCommercialOrdersAction(@RequestParam Integer orderCount) throws ResponseStatusException {
        LOGGER.info("ApiController.createCommercialOrdersAction(orderCount=" + orderCount + ")");

        List<CommercialOrder> commercialOrders;
        try {
            commercialOrders = randomCommercialOrderProducer.produce(orderCount);
        }
        catch (Exception exc) {
            exc.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), exc);
        }

        return commercialOrders;
    }

    /**
     * GET /topic/count
     *
     * @return the count of records for al consumed topics
     */
    @GetMapping("/topic/count")
    public Map<String, String> getTopicCount() {
        LOGGER.info("ApiController.getTopicCount()");

        Map<String, String> result = new HashMap<>();
        Map<String, Long> counters = recordCountRepository.countRecords();
        counters.forEach((String key, Long value) -> {
            result.put(key, (value < 0) ? "&#9888;" : Long.toString(value));
        });

        return result;
    }

    /**
     * GET /processes/status
     *
     * @return the status of all Kafka Streams processes
     */
    @GetMapping("/processes/status")
    public Map<String, Boolean> getProcessesStatus() {
        LOGGER.info("ApiController.getProcessesStatus()");

        boolean status;
        Map<String, Boolean> processesStatus = new HashMap<>();
        for (String qualifier : kafkaStreamProcessesStatusMonitor.getBeanQualifiers()) {
            try {
                status = kafkaStreamProcessesStatusMonitor.isRunning(qualifier);
            } catch (BeanNotFoundException exc) {
                exc.printStackTrace();
                status = false;
            }
            processesStatus.put(qualifier, status);
        }

        return processesStatus;
    }

    @PostMapping("/processes/{procid}/toggle")
    public void toggleProcessStatus(
            @PathVariable("procid") String procid
    ) throws ResponseStatusException {
        LOGGER.info("ApiController.toggleProcessStatus(prodid=" + procid + ")");

        try {
            if (kafkaStreamProcessesStatusMonitor.isRunning(procid)) {
                kafkaStreamProcessesStatusMonitor.stop(procid);
            } else {
                kafkaStreamProcessesStatusMonitor.start(procid);
            }
        } catch (BeanNotFoundException exc) {
            exc.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), exc);
        }
    }

    @PostMapping("/processes/start")
    public void startAllProcess() throws ResponseStatusException {
        LOGGER.info("ApiController.startAllProcess()");
        kafkaStreamProcessesStatusMonitor.startAll();
    }

    @PostMapping("/processes/stop")
    public void stopAllProcess() throws ResponseStatusException {
        LOGGER.info("ApiController.stopAllProcess()");
        kafkaStreamProcessesStatusMonitor.stopAll();
    }
}
