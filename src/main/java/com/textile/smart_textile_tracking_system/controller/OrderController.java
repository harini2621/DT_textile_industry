package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.OrderStatus;
import com.textile.smart_textile_tracking_system.service.ActivityLogService;
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
import java.util.List;


@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/create-order")
    public String createOrderPage(Model model) {
        model.addAttribute("order", new Order());
        return "create-order";
    }

    @PostMapping("/save-order")
    public String saveOrder(@ModelAttribute Order order,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        if (order == null || order.getProductName() == null || order.getProductName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Product name is required.");
            return "redirect:/create-order";
        }
        if (order.getQuantity() <= 0) {
            redirectAttributes.addFlashAttribute("error", "Quantity must be greater than zero.");
            return "redirect:/create-order";
        }
        order.setStatus(OrderStatus.PENDING.getLabel());
        order.setRequestDate(LocalDate.now());
        order.setOwnerUsername(userDetails.getUsername());
        orderService.saveOrder(order);
        activityLogService.log("ORDER_CREATION", userDetails.getUsername(),
                "Created order for product '" + order.getProductName() + "'");
        redirectAttributes.addFlashAttribute("success", "Order created successfully.");
        return "redirect:/owner-orders";
    }

    @GetMapping("/owner-orders")
    public String ownerOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        List<Order> orders = orderService.getOwnerOrders(username);

        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders",     orders.size());
        model.addAttribute("pendingOrders",   orders.stream().filter(o -> OrderStatus.PENDING.getLabel().equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("completedOrders", orders.stream().filter(o -> OrderStatus.COMPLETED.getLabel().equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("acceptedOrders",  orders.stream().filter(o -> OrderStatus.ACCEPTED.getLabel().equalsIgnoreCase(o.getStatus())).count());
        return "owner-orders";
    }

    @GetMapping("/worker-orders")
    public String workerOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        List<Order> orders = orderService.getWorkerOrders(username);

        model.addAttribute("orders", orders);
        model.addAttribute("totalOrders",     orders.size());
        model.addAttribute("pendingOrders",   orders.stream().filter(o -> OrderStatus.PENDING.getLabel().equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("completedOrders", orders.stream().filter(o -> OrderStatus.COMPLETED.getLabel().equalsIgnoreCase(o.getStatus())).count());
        model.addAttribute("inProgressOrders", orders.stream().filter(o -> OrderStatus.IN_PROGRESS.getLabel().equalsIgnoreCase(o.getStatus())).count());
        return "worker-orders";
    }

                    @PostMapping("/worker/accept")
    public String acceptOrder(@RequestParam Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        String username = userDetails.getUsername();
        if (order == null || !username.equals(order.getWorkerUsername())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or not assigned to you.");
            return "redirect:/worker-orders";
        }
        order.setStatus(OrderStatus.ACCEPTED.getLabel());
        orderService.saveOrder(order);
        activityLogService.log("ORDER_ACCEPT", username,
                "Accepted order '" + order.getProductName() + "' (id " + id + ")");
        redirectAttributes.addFlashAttribute("success", "Order accepted.");
        return "redirect:/worker-orders";
    }

    @PostMapping("/worker/reject")
    public String rejectOrder(@RequestParam Long id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        String username = userDetails.getUsername();
        if (order == null || !username.equals(order.getWorkerUsername())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or not assigned to you.");
            return "redirect:/worker-orders";
        }
        order.setStatus(OrderStatus.REJECTED.getLabel());
        orderService.saveOrder(order);
        activityLogService.log("ORDER_REJECT", username,
                "Rejected order '" + order.getProductName() + "' (id " + id + ")");
        redirectAttributes.addFlashAttribute("success", "Order rejected.");
        return "redirect:/worker-orders";
    }

    @PostMapping("/worker/update-date")
    public String updateDate(@RequestParam Long id,
                             @RequestParam("expectedDate") String expectedDate,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Order order = orderService.getOrder(id);
        String username = userDetails.getUsername();
        if (order == null || !username.equals(order.getWorkerUsername())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or not assigned to you.");
            return "redirect:/worker-orders";
        }
        try {
            order.setExpectedDeliveryDate(LocalDate.parse(expectedDate));
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Invalid delivery date.");
            return "redirect:/worker-orders";
        }
        order.setStatus(OrderStatus.IN_PROGRESS.getLabel());
        orderService.saveOrder(order);
        activityLogService.log("ORDER_UPDATE_DATE", username,
                "Updated delivery date for order (id " + id + ")");
        redirectAttributes.addFlashAttribute("success", "Delivery date updated.");
        return "redirect:/worker-orders";
    }

    @PostMapping("/worker/complete")
    public String completeOrder(@RequestParam Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        String username = userDetails.getUsername();
        if (order == null || !username.equals(order.getWorkerUsername())) {
            redirectAttributes.addFlashAttribute("error", "Order not found or not assigned to you.");
            return "redirect:/worker-orders";
        }
        orderService.completeOrder(id);
        activityLogService.log("ORDER_COMPLETE", username,
                "Completed order '" + order.getProductName() + "' (id " + id + ")");
        redirectAttributes.addFlashAttribute("success", "Order marked as completed.");
        return "redirect:/worker-orders";
    }
}
