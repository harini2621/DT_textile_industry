package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Total quantity of every stock row, summed by the database.
    @Query("SELECT SUM(s.quantity) FROM Stock s")
    Long sumQuantity();

    // Total quantity held by a single worker, summed by the database.
    @Query("SELECT SUM(s.quantity) FROM Stock s WHERE s.workerUsername = :workerUsername")
    Long sumQuantityByWorkerUsername(@Param("workerUsername") String workerUsername);

}