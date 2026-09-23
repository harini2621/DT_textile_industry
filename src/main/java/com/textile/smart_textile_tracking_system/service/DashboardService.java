package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.dto.DashboardStats;
import com.textile.smart_textile_tracking_system.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Centralizes the calculations required by the owner and worker dashboards.
 * Moved out of DashboardController so the controller only orchestrates
 * the request/response flow and stays free of business logic.
 */
@Service
public class DashboardService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StockService stockService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    public DashboardStats getOwnerDashboardStats(String username) {
        DashboardStats stats = new DashboardStats();

        long totalOrders = orderService.countOrders();
        long pendingOrders = orderService.countOrdersByStatus(OrderStatus.PENDING.getLabel());
        long completedOrders = orderService.countOrdersByStatus(OrderStatus.COMPLETED.getLabel());
        long inProgressOrders = orderService.countOrdersByStatus(OrderStatus.IN_PROGRESS.getLabel());
        long totalStock = stockService.getTotalStockQuantity();
        long totalWorkers = workerService.countWorkers();

        stats.setTotalOrders(totalOrders);
        stats.setPendingOrders(pendingOrders);
        stats.setInProgressOrders(inProgressOrders);
        stats.setCompletedOrders(completedOrders);
        stats.setTotalStock(totalStock);
        stats.setTotalWorkers(totalWorkers);
        stats.setRecentOrders(orderService.getRecentOrders(5));

        userService.getUserByUsername(username).ifPresent(stats::setLoggedUser);

        return stats;
    }

    public DashboardStats getWorkerDashboardStats(String username) {
        DashboardStats stats = new DashboardStats();

        long assignedTasks = taskService.countTasksByAssignedWorker(username);
        long completedTasks = taskService.countTasksByAssignedWorkerAndStatus(username, "Completed");
        long pendingTasks = taskService.countTasksByAssignedWorkerAndStatus(username, "Pending");
        long myStockCount = stockService.getWorkerStockQuantity(username);

        stats.setAssignedTasks(assignedTasks);
        stats.setCompletedTasks(completedTasks);
        stats.setPendingTasks(pendingTasks);
        stats.setMyStockCount(myStockCount);
        stats.setMyOrders(orderService.getWorkerOrders(username));

        userService.getUserByUsername(username).ifPresent(stats::setLoggedUser);

        return stats;
    }
}
