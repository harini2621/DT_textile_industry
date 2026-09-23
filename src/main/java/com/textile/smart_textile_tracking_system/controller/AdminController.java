package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.repository.UserRepository;
import com.textile.smart_textile_tracking_system.service.ActivityLogService;
import com.textile.smart_textile_tracking_system.service.NotificationService;
import com.textile.smart_textile_tracking_system.service.OrderService;
import com.textile.smart_textile_tracking_system.service.ProductionOrderService;
import com.textile.smart_textile_tracking_system.service.StockService;
import com.textile.smart_textile_tracking_system.service.TaskService;
import com.textile.smart_textile_tracking_system.service.UserService;
import com.textile.smart_textile_tracking_system.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductionOrderService productionOrderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private NotificationService notificationService;

    // ================= ANALYTICS DASHBOARD =================

    @GetMapping("/dashboard")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        List<User> allUsers = userRepository.findAll();

        long totalUsers   = allUsers.size();
        long totalOwners  = userRepository.countByRole("OWNER");
        long totalWorkers = userRepository.countByRole("WORKER");
        long totalAdmins  = userRepository.countByRole("ADMIN");
        long pendingUsers = userService.countPendingUsers();

        long totalProductionOrders = productionOrderService.countProductionOrders();
        long totalTasks            = taskService.countAllTasks();
        long totalOrders           = orderService.countOrders();
        long totalStock            = stockService.getTotalStockQuantity();
        long totalWorkerRecords    = workerService.countWorkers();

        model.addAttribute("totalUsers",   totalUsers);
        model.addAttribute("totalOwners",  totalOwners);
        model.addAttribute("totalWorkers", totalWorkers);
        model.addAttribute("totalAdmins",  totalAdmins);
        model.addAttribute("pendingUsers", pendingUsers);

        model.addAttribute("totalProductionOrders", totalProductionOrders);
        model.addAttribute("totalTasks",            totalTasks);
        model.addAttribute("totalOrders",           totalOrders);
        model.addAttribute("totalStock",            totalStock);
        model.addAttribute("totalWorkerRecords",    totalWorkerRecords);

        model.addAttribute("completedTasks",  taskService.countTasksByStatus("Completed"));
        model.addAttribute("pendingTasks",    taskService.countTasksByStatus("Pending"));
        model.addAttribute("inProgressTasks", taskService.countTasksByStatus("In Progress"));

        model.addAttribute("unreadNotifications", notificationService.getUnreadCount());
        model.addAttribute("totalLogs",           activityLogService.getTotalLogs());

        // Recent registrations (newest first, capped at 5 for the dashboard).
        List<User> recentRegistrations = allUsers.stream()
                .filter(u -> u.getUserId() != null)
                .sorted((a, b) -> b.getUserId().compareTo(a.getUserId()))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("recentRegistrations", recentRegistrations);

        model.addAttribute("recentLogs", activityLogService.getAllLogs().stream()
                .limit(5).collect(Collectors.toList()));

        userService.getUserByUsername(userDetails.getUsername())
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "admin-dashboard";
    }

    // ================= USER APPROVAL =================

    @GetMapping("/users")
    public String adminUsers(Model model) {
        model.addAttribute("allUsers",      userRepository.findAll());
        model.addAttribute("pendingUsersList", userService.getPendingUsers());
        model.addAttribute("pendingCount",  userService.getPendingUsers().size());
        model.addAttribute("unreadNotifications", notificationService.getUnreadCount());
        return "admin-users";
    }

    @PostMapping("/users/approve")
    public String approveUser(@RequestParam Long userId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        userService.approveUser(userId);
        activityLogService.log("USER_APPROVED", userDetails.getUsername(),
                "Approved user id " + userId);
        notificationService.createNotification(
                "User Approved",
                "User id " + userId + " was approved by " + userDetails.getUsername(),
                userDetails.getUsername());
        redirectAttributes.addFlashAttribute("success", "User approved successfully.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/reject")
    public String rejectUser(@RequestParam Long userId,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        userService.rejectUser(userId);
        activityLogService.log("USER_REJECTED", userDetails.getUsername(),
                "Rejected user id " + userId);
        redirectAttributes.addFlashAttribute("success", "User rejected.");
        return "redirect:/admin/users";
    }

    // ================= ACTIVITY LOGS =================

    @GetMapping("/activity-logs")
    public String activityLogs(@RequestParam(value = "action", required = false) String action,
                               Model model) {
        if (action != null && !action.isBlank()) {
            model.addAttribute("logs", activityLogService.getLogsByAction(action));
            model.addAttribute("selectedAction", action);
        } else {
            model.addAttribute("logs", activityLogService.getAllLogs());
        }
        model.addAttribute("totalLogs", activityLogService.getTotalLogs());
        model.addAttribute("unreadNotifications", notificationService.getUnreadCount());
        return "admin-activity-logs";
    }

    // ================= NOTIFICATIONS =================

    @GetMapping("/notifications")
    public String notifications(Model model) {
        model.addAttribute("notifications", notificationService.getAllNotifications());
        model.addAttribute("unreadNotifications", notificationService.getUnreadCount());
        return "admin-notifications";
    }

    @PostMapping("/notifications/read")
    public String markNotificationRead(@RequestParam Long notificationId,
                                       RedirectAttributes redirectAttributes) {
        notificationService.markAsRead(notificationId);
        redirectAttributes.addFlashAttribute("success", "Notification marked as read.");
        return "redirect:/admin/notifications";
    }

    @PostMapping("/notifications/read-all")
    public String markAllRead(RedirectAttributes redirectAttributes) {
        notificationService.markAllAsRead();
        redirectAttributes.addFlashAttribute("success", "All notifications marked as read.");
        return "redirect:/admin/notifications";
    }
}
