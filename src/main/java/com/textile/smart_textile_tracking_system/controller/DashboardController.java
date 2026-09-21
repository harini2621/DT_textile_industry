package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.service.OrderService;
import com.textile.smart_textile_tracking_system.service.StockService;
import com.textile.smart_textile_tracking_system.service.TaskService;
import com.textile.smart_textile_tracking_system.service.UserService;
import com.textile.smart_textile_tracking_system.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockService stockService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/owner-dashboard")
    public String ownerDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        long totalOrders   = orderService.getAllOrders().size();
        long pendingOrders = orderService.getOrdersByStatus("PENDING").size();
        long completedOrders = orderService.getOrdersByStatus("Completed").size();
        long totalStock    = stockService.getAllStocks().stream().mapToLong(s -> s.getQuantity()).sum();
        long totalWorkers  = workerService.getAllWorkers().size();

        model.addAttribute("totalOrders",    totalOrders);
        model.addAttribute("pendingOrders",  pendingOrders);
        model.addAttribute("completedOrders",completedOrders);
        model.addAttribute("totalStock",     totalStock);
        model.addAttribute("totalWorkers",   totalWorkers);
        model.addAttribute("recentOrders",   orderService.getRecentOrders(5));

        userService.getUserByUsername(userDetails.getUsername())
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "owner-dashboard";
    }

    @GetMapping("/worker-dashboard")
    public String workerDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String username = userDetails.getUsername();

        long assignedTasks  = taskService.getTasksByAssignedWorker(username).size();
        long completedTasks = taskService.getTasksByAssignedWorkerAndStatus(username, "Completed").size();
        long pendingTasks   = taskService.getTasksByAssignedWorkerAndStatus(username, "Pending").size();
        long myStockCount   = stockService.getWorkerStocks(username).stream().mapToLong(s -> s.getQuantity()).sum();

        model.addAttribute("assignedTasks",  assignedTasks);
        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("pendingTasks",   pendingTasks);
        model.addAttribute("myStockCount",   myStockCount);
        model.addAttribute("myOrders",       orderService.getWorkerOrders(username));

        userService.getUserByUsername(username)
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "worker-dashboard";
    }
}
