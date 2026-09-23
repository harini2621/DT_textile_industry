package com.textile.smart_textile_tracking_system.dto;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.User;

import java.util.List;

/**
 * Aggregated statistics used by both the owner and worker dashboards.
 * Only the fields relevant to a given dashboard are populated by
 * {@code DashboardService}; the rest keep their default values.
 */
public class DashboardStats {

    // ---- Owner dashboard fields ----
    private long totalOrders;
    private long pendingOrders;
    private long completedOrders;
    private long inProgressOrders;
    private long totalWorkers;
    private long totalStock;
    private List<Order> recentOrders;

    // ---- Worker dashboard fields ----
    private long assignedTasks;
    private long completedTasks;
    private long pendingTasks;
    private long myStockCount;
    private List<Order> myOrders;

    // ---- Shared ----
    private User loggedUser;

    public DashboardStats() {
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public long getInProgressOrders() {
        return inProgressOrders;
    }

    public void setInProgressOrders(long inProgressOrders) {
        this.inProgressOrders = inProgressOrders;
    }

    public long getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(long totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(long totalStock) {
        this.totalStock = totalStock;
    }

    public List<Order> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<Order> recentOrders) {
        this.recentOrders = recentOrders;
    }

    public long getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(long assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(long pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public long getMyStockCount() {
        return myStockCount;
    }

    public void setMyStockCount(long myStockCount) {
        this.myStockCount = myStockCount;
    }

    public List<Order> getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
