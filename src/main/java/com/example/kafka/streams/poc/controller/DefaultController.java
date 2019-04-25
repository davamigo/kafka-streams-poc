package com.example.kafka.streams.poc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    /**
     * GET /
     *
     * Default action - shows the homepage.
     *
     * @return the name of the template to load (homepage.html)
     */
    @GetMapping("/")
    public String homepage() {
        return "default/homepage";
    }
}
