package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.dto.DashboardStats;
import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the calculations moved out of DashboardController into DashboardService.
 */
class DashboardServiceTest {

    private final OrderService orderService = Mockito.mock(OrderService.class);
    private final StockService stockService = Mockito.mock(StockService.class);
    private final WorkerService workerService = Mockito.mock(WorkerService.class);
    private final TaskService taskService = Mockito.mock(TaskService.class);
    private final UserService userService = Mockito.mock(UserService.class);

    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        dashboardService = new DashboardService();
        ReflectionTestUtils.setField(dashboardService, "orderService", orderService);
        ReflectionTestUtils.setField(dashboardService, "stockService", stockService);
        ReflectionTestUtils.setField(dashboardService, "workerService", workerService);
        ReflectionTestUtils.setField(dashboardService, "taskService", taskService);
        ReflectionTestUtils.setField(dashboardService, "userService", userService);
    }

    private static User user(String username) {
        User user = new User();
        user.setUsername(username);
        return user;
    }

    @Test
    void ownerDashboardStatsAggregatesOrdersStockWorkersAndRecentOrders() {
        when(orderService.countOrders()).thenReturn(2L);
        when(orderService.countOrdersByStatus("PENDING")).thenReturn(1L);
        when(orderService.countOrdersByStatus("Completed")).thenReturn(0L);
        when(orderService.countOrdersByStatus("IN PROGRESS")).thenReturn(1L);
        when(orderService.getRecentOrders(5)).thenReturn(List.of(new Order()));
        when(stockService.getTotalStockQuantity()).thenReturn(10L);
        when(workerService.countWorkers()).thenReturn(3L);
        when(userService.getUserByUsername("owner1")).thenReturn(Optional.of(user("owner1")));

        DashboardStats stats = dashboardService.getOwnerDashboardStats("owner1");

        assertEquals(2L, stats.getTotalOrders());
        assertEquals(1L, stats.getPendingOrders());
        assertEquals(0L, stats.getCompletedOrders());
        assertEquals(1L, stats.getInProgressOrders());
        assertEquals(10L, stats.getTotalStock());
        assertEquals(3L, stats.getTotalWorkers());
        assertEquals(1, stats.getRecentOrders().size());
        assertEquals("owner1", stats.getLoggedUser().getUsername());
    }

    @Test
    void ownerDashboardStatsWithoutKnownUserLeavesLoggedUserNull() {
        when(orderService.countOrders()).thenReturn(0L);
        when(orderService.countOrdersByStatus(Mockito.anyString())).thenReturn(0L);
        when(orderService.getRecentOrders(5)).thenReturn(List.of());
        when(stockService.getTotalStockQuantity()).thenReturn(0L);
        when(workerService.countWorkers()).thenReturn(0L);
        when(userService.getUserByUsername("unknown")).thenReturn(Optional.empty());

        DashboardStats stats = dashboardService.getOwnerDashboardStats("unknown");

        assertEquals(0L, stats.getTotalOrders());
        assertNull(stats.getLoggedUser());
    }

    @Test
    void workerDashboardStatsAggregatesTasksStockAndOrders() {
        when(taskService.countTasksByAssignedWorker("worker1")).thenReturn(3L);
        when(taskService.countTasksByAssignedWorkerAndStatus("worker1", "Completed")).thenReturn(1L);
        when(taskService.countTasksByAssignedWorkerAndStatus("worker1", "Pending")).thenReturn(1L);
        when(stockService.getWorkerStockQuantity("worker1")).thenReturn(10L);
        when(orderService.getWorkerOrders("worker1")).thenReturn(List.of(new Order()));
        when(userService.getUserByUsername("worker1")).thenReturn(Optional.of(user("worker1")));

        DashboardStats stats = dashboardService.getWorkerDashboardStats("worker1");

        assertEquals(3L, stats.getAssignedTasks());
        assertEquals(1L, stats.getCompletedTasks());
        assertEquals(1L, stats.getPendingTasks());
        assertEquals(10L, stats.getMyStockCount());
        assertEquals(1, stats.getMyOrders().size());
        assertEquals("worker1", stats.getLoggedUser().getUsername());
    }
}
