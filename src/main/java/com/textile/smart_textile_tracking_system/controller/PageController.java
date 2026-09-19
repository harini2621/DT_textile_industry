package com.textile.smart_textile_tracking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/workers")
    public String workers() {
        return "workers";
    }

    @GetMapping("/orders")
    public String orders() {
        return "production-orders";
    }

    @GetMapping("/tasks")
    public String tasks() {
        return "tasks";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }
}