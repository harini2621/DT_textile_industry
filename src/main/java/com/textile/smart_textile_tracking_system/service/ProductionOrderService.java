package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.ProductionOrder;
import com.textile.smart_textile_tracking_system.repository.ProductionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductionOrderService {

    @Autowired
    private ProductionOrderRepository productionOrderRepository;

    // Save Production Order
    public ProductionOrder saveProductionOrder(ProductionOrder productionOrder) {
        return productionOrderRepository.save(productionOrder);
    }

    // Get All Production Orders
    public List<ProductionOrder> getAllProductionOrders() {
        return productionOrderRepository.findAll();
    }

    // Count Production Orders
    public long countProductionOrders() {
        return productionOrderRepository.count();
    }

    // Get Production Order By ID
    public Optional<ProductionOrder> getProductionOrderById(Long id) {
        return productionOrderRepository.findById(id);
    }

    // Get Orders By Status
    public List<ProductionOrder> getOrdersByStatus(String status) {
        return productionOrderRepository.findByStatus(status);
    }

    // Get Orders By Product Name
    public List<ProductionOrder> getOrdersByProductName(String productName) {
        return productionOrderRepository.findByProductName(productName);
    }

    // Delete Production Order
    public void deleteProductionOrder(Long id) {
        productionOrderRepository.deleteById(id);
    }
}