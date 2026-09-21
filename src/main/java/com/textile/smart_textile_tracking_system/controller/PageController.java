package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.service.OrderService;
import com.textile.smart_textile_tracking_system.service.StockService;
import com.textile.smart_textile_tracking_system.service.TaskService;
import com.textile.smart_textile_tracking_system.service.WorkerService;
import com.textile.smart_textile_tracking_system.service.ProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired private WorkerService workerService;
    @Autowired private TaskService taskService;
    @Autowired private ProductionOrderService productionOrderService;
    @Autowired private OrderService orderService;
    @Autowired private StockService stockService;

    @GetMapping("/workers")
    public String workers(Model model) {
        model.addAttribute("workers",          workerService.getAllWorkers());
        model.addAttribute("totalWorkers",     workerService.getAllWorkers().size());
        model.addAttribute("availableWorkers", workerService.getWorkersByAvailability("Available").size());
        model.addAttribute("busyWorkers",      workerService.getWorkersByAvailability("Busy").size());
        return "workers";
    }

    @GetMapping("/tasks")
    public String tasks(Model model) {
        model.addAttribute("tasks",           taskService.getAllTasks());
        model.addAttribute("totalTasks",      taskService.getAllTasks().size());
        model.addAttribute("completedTasks",  taskService.getTasksByStatus("Completed").size());
        model.addAttribute("pendingTasks",    taskService.getTasksByStatus("Pending").size());
        model.addAttribute("inProgressTasks", taskService.getTasksByStatus("In Progress").size());
        return "tasks";
    }

    @GetMapping("/production-orders")
    public String productionOrders(Model model) {
        model.addAttribute("productionOrders",  productionOrderService.getAllProductionOrders());
        model.addAttribute("totalOrders",       productionOrderService.getAllProductionOrders().size());
        model.addAttribute("completedOrders",   productionOrderService.getOrdersByStatus("Completed").size());
        model.addAttribute("pendingOrders",     productionOrderService.getOrdersByStatus("Pending").size());
        model.addAttribute("inProgressOrders",  productionOrderService.getOrdersByStatus("In Progress").size());
        return "production-orders";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        long totalOrders     = orderService.getAllOrders().size();
        long completedOrders = orderService.getOrdersByStatus("Completed").size();
        long pendingOrders   = orderService.getOrdersByStatus("PENDING").size();
        long totalStock      = stockService.getAllStocks().stream().mapToLong(s -> s.getQuantity()).sum();
        long totalWorkers    = workerService.getAllWorkers().size();

        model.addAttribute("totalOrders",     totalOrders);
        model.addAttribute("completedOrders", completedOrders);
        model.addAttribute("pendingOrders",   pendingOrders);
        model.addAttribute("totalStock",      totalStock);
        model.addAttribute("totalWorkers",    totalWorkers);
        return "reports";
    }
}
