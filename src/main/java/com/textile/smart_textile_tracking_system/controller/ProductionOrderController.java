package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.ProductionOrder;
import com.textile.smart_textile_tracking_system.service.ProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class ProductionOrderController {

    @Autowired
    private ProductionOrderService productionOrderService;

    @PostMapping
    public ProductionOrder saveProductionOrder(@RequestBody ProductionOrder productionOrder) {
        return productionOrderService.saveProductionOrder(productionOrder);
    }

    @GetMapping
    public List<ProductionOrder> getAllProductionOrders() {
        return productionOrderService.getAllProductionOrders();
    }

    @GetMapping("/{id}")
    public Optional<ProductionOrder> getProductionOrderById(@PathVariable Long id) {
        return productionOrderService.getProductionOrderById(id);
    }

    @GetMapping("/status/{status}")
    public List<ProductionOrder> getOrdersByStatus(@PathVariable String status) {
        return productionOrderService.getOrdersByStatus(status);
    }

    @GetMapping("/product/{productName}")
    public List<ProductionOrder> getOrdersByProductName(@PathVariable String productName) {
        return productionOrderService.getOrdersByProductName(productName);
    }

    @DeleteMapping("/{id}")
    public String deleteProductionOrder(@PathVariable Long id) {
        productionOrderService.deleteProductionOrder(id);
        return "Production Order deleted successfully";
    }
}