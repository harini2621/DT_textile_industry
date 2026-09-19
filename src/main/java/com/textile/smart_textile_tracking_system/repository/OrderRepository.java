package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Worker orders
    List<Order> findByWorkerUsername(String workerUsername);

    // Owner orders
    List<Order> findByOwnerUsername(String ownerUsername);

    // Search by product
    List<Order> findByProductNameContainingIgnoreCase(String productName);

    // Filter by status
    List<Order> findByStatus(String status);

}