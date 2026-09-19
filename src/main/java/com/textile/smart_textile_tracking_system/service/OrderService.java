package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Save Order
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Get All Orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Worker Orders
    public List<Order> getWorkerOrders(String username) {
        return orderRepository.findByWorkerUsername(username);
    }

    // Owner Orders
    public List<Order> getOwnerOrders(String username) {
        return orderRepository.findByOwnerUsername(username);
    }

    // Get Order by ID
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Search Product
    public List<Order> searchProduct(String productName) {
        return orderRepository.findByProductNameContainingIgnoreCase(productName);
    }

    // Accept Order
    public void acceptOrder(Long id) {

        Order order = getOrder(id);

        if (order != null) {
            order.setStatus("Accepted");
            orderRepository.save(order);
        }

    }

    // Reject Order
    public void rejectOrder(Long id) {

        Order order = getOrder(id);

        if (order != null) {
            order.setStatus("Rejected");
            orderRepository.save(order);
        }

    }

    // Update Expected Delivery Date
    public void updateDeliveryDate(Long id, LocalDate date) {

        Order order = getOrder(id);

        if (order != null) {
            order.setExpectedDeliveryDate(date);
            orderRepository.save(order);
        }

    }

    // Complete Order
    public void completeOrder(Long id) {

        Order order = getOrder(id);

        if (order != null) {
            order.setStatus("Completed");
            orderRepository.save(order);
        }

    }

}