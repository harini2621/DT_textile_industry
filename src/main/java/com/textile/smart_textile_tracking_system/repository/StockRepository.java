package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Search by Product Name
    List<Stock> findByProductNameContainingIgnoreCase(String productName);

    // Worker Stock
    List<Stock> findByWorkerUsername(String workerUsername);

    // Search by Product + Quantity > 0
    List<Stock> findByProductNameContainingIgnoreCaseAndQuantityGreaterThan(
            String productName, int quantity);

}