package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

    List<ProductionOrder> findByStatus(String status);

    List<ProductionOrder> findByProductName(String productName);

    List<ProductionOrder> findByCurrentStage(String currentStage);

    List<ProductionOrder> findByAssignedWorker(String assignedWorker);

}