package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.OrderStatus;
import com.textile.smart_textile_tracking_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getWorkerOrders(String username) {
        return orderRepository.findByWorkerUsername(username);
    }

    public List<Order> getOwnerOrders(String username) {
        return orderRepository.findByOwnerUsername(username);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> searchProduct(String productName) {
        return orderRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatusIgnoreCase(status);
    }

    public List<Order> getRecentOrders(int limit) {
        List<Order> all = orderRepository.findAllByOrderByIdDesc();
        return all.size() > limit ? all.subList(0, limit) : all;
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public long countOrdersByStatus(String status) {
        return orderRepository.countByStatusIgnoreCase(status);
    }

    public void acceptOrder(Long id) {
        Order order = getOrder(id);
        if (order != null) {
            order.setStatus(OrderStatus.ACCEPTED.getLabel());
            orderRepository.save(order);
        }
    }

    public void rejectOrder(Long id) {
        Order order = getOrder(id);
        if (order != null) {
            order.setStatus(OrderStatus.REJECTED.getLabel());
            orderRepository.save(order);
        }
    }

    public void updateDeliveryDate(Long id, LocalDate date) {
        Order order = getOrder(id);
        if (order != null) {
            order.setExpectedDeliveryDate(date);
            orderRepository.save(order);
        }
    }

    public void completeOrder(Long id) {
        Order order = getOrder(id);
        if (order != null) {
            order.setStatus(OrderStatus.COMPLETED.getLabel());
            orderRepository.save(order);
        }
    }
}
