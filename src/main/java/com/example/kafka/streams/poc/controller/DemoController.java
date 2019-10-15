package com.example.kafka.streams.poc.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Demo controller.
 * Base route: /demo
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    /** Logger object */
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    /**
     * GET /demo
     *
     * Shows the demo homepage.
     *
     * @return the model and view for the template demohome.html
     */
    @GetMapping({"", "/"})
    public ModelAndView demoHomePage() {
        LOGGER.info("DemoController.demoHomePage()");
        final ModelAndView mav = new ModelAndView("demo/demohome");
        return mav;
    }

    /**
     * GET /demo/long
     *
     * Shows the long homepage.
     *
     * @return the model and view for the template longdemohome.html
     */
    @GetMapping("/long")
    public ModelAndView longDemoHomePage() {
        LOGGER.info("DemoController.demoHomePage()");
        final ModelAndView mav = new ModelAndView("demo/longdemohome");
        return mav;
    }
}
