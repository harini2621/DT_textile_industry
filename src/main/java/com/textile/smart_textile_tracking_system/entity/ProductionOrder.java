package com.textile.smart_textile_tracking_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "production_orders")
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String deadline;

    // Process stage tracking: Cutting -> Stitching -> Dyeing -> Finishing -> Packing -> Completed.
    // Nullable so existing rows are unaffected.
    @Column(name = "current_stage")
    private String currentStage;

    @Column(name = "assigned_worker")
    private String assignedWorker;

    public ProductionOrder() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public String getAssignedWorker() {
        return assignedWorker;
    }

    public void setAssignedWorker(String assignedWorker) {
        this.assignedWorker = assignedWorker;
    }
}