package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByWorkerUsername(String workerUsername);

    List<Order> findByOwnerUsername(String ownerUsername);

    List<Order> findByProductNameContainingIgnoreCase(String productName);

    List<Order> findByStatus(String status);

    List<Order> findByStatusIgnoreCase(String status);

    List<Order> findAllByOrderByIdDesc();

    // Dashboard count: avoids loading every order just to count them.
    long countByStatusIgnoreCase(String status);
}
