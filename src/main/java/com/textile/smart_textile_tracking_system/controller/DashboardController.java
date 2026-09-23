package com.textile.smart_textile_tracking_system.controller;
import com.textile.smart_textile_tracking_system.dto.DashboardStats;
import com.textile.smart_textile_tracking_system.service.DashboardService;
import com.textile.smart_textile_tracking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping("/owner-dashboard")
    public String ownerDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        String username = userDetails.getUsername();

        DashboardStats stats = dashboardService.getOwnerDashboardStats(username);

        model.addAttribute("totalOrders", stats.getTotalOrders());
        model.addAttribute("pendingOrders", stats.getPendingOrders());
        model.addAttribute("inProgressOrders", stats.getInProgressOrders());
        model.addAttribute("completedOrders", stats.getCompletedOrders());
        model.addAttribute("totalStock", stats.getTotalStock());
        model.addAttribute("totalWorkers", stats.getTotalWorkers());
        model.addAttribute("recentOrders", stats.getRecentOrders());

        addLoggedUser(model, username);

        return "owner-dashboard";
    }

    @GetMapping("/worker-dashboard")
    public String workerDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        String username = userDetails.getUsername();

        DashboardStats stats = dashboardService.getWorkerDashboardStats(username);

        model.addAttribute("assignedTasks", stats.getAssignedTasks());
        model.addAttribute("completedTasks", stats.getCompletedTasks());
        model.addAttribute("pendingTasks", stats.getPendingTasks());
        model.addAttribute("myStockCount", stats.getMyStockCount());
        model.addAttribute("myOrders", stats.getMyOrders());

        addLoggedUser(model, username);

        return "worker-dashboard";
    }

    /**
     * Adds the currently logged-in user to the model so the dashboard layouts
     * can greet them. Nothing is added when the username is unknown.
     */
    private void addLoggedUser(Model model, String username) {
        userService.getUserByUsername(username)
                   .ifPresent(user -> model.addAttribute("loggedUser", user));
    }
}

