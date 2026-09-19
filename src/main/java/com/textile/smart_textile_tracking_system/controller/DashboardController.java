package com.textile.smart_textile_tracking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/owner-dashboard")
    public String ownerDashboard() {
        return "owner-dashboard";
    }

    @GetMapping("/worker-dashboard")
    public String workerDashboard() {
        return "worker-dashboard";
    }

}