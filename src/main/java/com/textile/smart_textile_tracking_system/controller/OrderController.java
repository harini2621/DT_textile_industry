package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.service.OrderService;
import com.textile.smart_textile_tracking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/create-order")
    public String createOrderPage(Model model) {
        model.addAttribute("order", new Order());
        return "create-order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute Order order,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        order.setStatus("PENDING");
        order.setRequestDate(LocalDate.now());
        order.setOwnerUsername(userDetails.getUsername());
        orderService.saveOrder(order);
        redirectAttributes.addFlashAttribute("success", "Order created successfully.");
        return "redirect:/owner-orders";
    }

    @GetMapping("/owner-orders")
    public String ownerOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("orders", orderService.getOwnerOrders(username));
        model.addAttribute("totalOrders",     orderService.getOwnerOrders(username).size());
        model.addAttribute("pendingOrders",   orderService.getOwnerOrders(username).stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("completedOrders", orderService.getOwnerOrders(username).stream().filter(o -> "Completed".equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("acceptedOrders",  orderService.getOwnerOrders(username).stream().filter(o -> "Accepted".equalsIgnoreCase(o.getStatus())).count());
        return "owner-orders";
    }

    @GetMapping("/worker-orders")
    public String workerOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("orders", orderService.getWorkerOrders(username));
        model.addAttribute("totalOrders",     orderService.getWorkerOrders(username).size());
        model.addAttribute("pendingOrders",   orderService.getWorkerOrders(username).stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("completedOrders", orderService.getWorkerOrders(username).stream().filter(o -> "Completed".equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("inProgressOrders",orderService.getWorkerOrders(username).stream().filter(o -> "IN PROGRESS".equalsIgnoreCase(o.getStatus())).count());
        return "worker-orders";
    }

    @PostMapping("/worker/accept")
    public String acceptOrder(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        if (order != null) {
            order.setStatus("Accepted");
            orderService.saveOrder(order);
        }
        redirectAttributes.addFlashAttribute("success", "Order accepted.");
        return "redirect:/worker-orders";
    }

    @PostMapping("/worker/reject")
    public String rejectOrder(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        if (order != null) {
            order.setStatus("Rejected");
            orderService.saveOrder(order);
        }
        redirectAttributes.addFlashAttribute("success", "Order rejected.");
        return "redirect:/worker-orders";
    }

    @PostMapping("/worker/update-date")
    public String updateDate(@RequestParam Long id,
                             @RequestParam("expectedDate") String expectedDate,
                             RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        if (order != null) {
            order.setExpectedDeliveryDate(LocalDate.parse(expectedDate));
            order.setStatus("IN PROGRESS");
            orderService.saveOrder(order);
        }
        redirectAttributes.addFlashAttribute("success", "Delivery date updated.");
        return "redirect:/worker-orders";
    }

    @PostMapping("/worker/complete")
    public String completeOrder(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        orderService.completeOrder(id);
        redirectAttributes.addFlashAttribute("success", "Order marked as completed.");
        return "redirect:/worker-orders";
    }
}
