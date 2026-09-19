package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create Order Page
    @GetMapping("/create-order")
    public String createOrderPage(Model model) {
        model.addAttribute("order", new Order());
        return "create-order";
    }

    // Save Order
    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute Order order) {

        order.setStatus("PENDING");
        order.setRequestDate(LocalDate.now());

        orderService.saveOrder(order);

        return "redirect:/owner-orders";
    }

    // Owner Orders
    @GetMapping("/owner-orders")
    public String ownerOrders(Model model) {

        model.addAttribute("orders", orderService.getAllOrders());

        return "owner-orders";
    }

    // Worker Orders
    @GetMapping("/worker-orders")
    public String workerOrders(Model model) {

        model.addAttribute("orders", orderService.getAllOrders());

        return "worker-orders";
    }

    // Accept Order
    @PostMapping("/worker/accept")
    public String acceptOrder(@RequestParam Long id) {

        Order order = orderService.getOrder(id);

        if (order != null) {
            order.setStatus("ACCEPTED");
            orderService.saveOrder(order);
        }

        return "redirect:/worker-orders";
    }

    // Reject Order
    @PostMapping("/worker/reject")
    public String rejectOrder(@RequestParam Long id) {

        Order order = orderService.getOrder(id);

        if (order != null) {
            order.setStatus("REJECTED");
            orderService.saveOrder(order);
        }

        return "redirect:/worker-orders";
    }

    // Update Delivery Date
    @PostMapping("/worker/update-date")
    public String updateDate(@RequestParam Long id,
                             @RequestParam("expectedDate") String expectedDate) {

        Order order = orderService.getOrder(id);

        if (order != null) {

            order.setExpectedDeliveryDate(LocalDate.parse(expectedDate));
            order.setStatus("IN PROGRESS");

            orderService.saveOrder(order);
        }

        return "redirect:/worker-orders";
    }

}