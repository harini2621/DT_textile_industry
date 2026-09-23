package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Payment;
import com.textile.smart_textile_tracking_system.entity.Task;
import com.textile.smart_textile_tracking_system.service.NotificationService;
import com.textile.smart_textile_tracking_system.service.OrderService;
import com.textile.smart_textile_tracking_system.service.PaymentService;
import com.textile.smart_textile_tracking_system.service.TaskService;
import com.textile.smart_textile_tracking_system.service.UserService;
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

@Controller
@RequestMapping("/worker")
public class WorkerModuleController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/my-tasks")
    public String myTasks(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        List<Task> tasks = taskService.getTasksByAssignedWorker(username);

        long total     = tasks.size();
        long completed = tasks.stream().filter(t -> "Completed".equals(t.getStatus())).count();
        long inProgress= tasks.stream().filter(t -> "In Progress".equals(t.getStatus())).count();
        long pending   = tasks.stream().filter(t -> "Pending".equals(t.getStatus())).count();

        model.addAttribute("tasks",          tasks);
        model.addAttribute("totalTasks",     total);
        model.addAttribute("completedTasks", completed);
        model.addAttribute("inProgressTasks",inProgress);
        model.addAttribute("pendingTasks",   pending);

        userService.getUserByUsername(username)
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "worker-my-tasks";
    }

    @PostMapping("/task/update-status")
    public String updateTaskStatus(@RequestParam Long taskId,
                                   @RequestParam String status,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        taskService.getTaskById(taskId)
                .filter(task -> username.equals(task.getAssignedWorker()))
                .ifPresent(task -> {
                    task.setStatus(status);
                    taskService.saveTask(task);
                });
        redirectAttributes.addFlashAttribute("success", "Task status updated to: " + status);
        return "redirect:/worker/my-tasks";
    }

    @GetMapping("/work-history")
    public String workHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        var allOrders = orderService.getWorkerOrders(username);
        var completed = allOrders.stream()
                .filter(o -> "Completed".equalsIgnoreCase(o.getStatus()))
                .toList();

        model.addAttribute("orders",          completed);
        model.addAttribute("totalCompleted",  completed.size());
        model.addAttribute("totalOrders",     allOrders.size());

        userService.getUserByUsername(username)
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "worker-work-history";
    }

    @GetMapping("/payment-history")
    public String paymentHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();

        var payments = paymentService.getWorkerPayments(username);
        double totalPaid = payments.stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getPaymentStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();

        model.addAttribute("payments",   payments);
        model.addAttribute("totalPaid",  totalPaid);
        model.addAttribute("totalJobs",  payments.size());
        model.addAttribute("totalQuantity", payments.stream()
                .mapToInt(p -> p.getOrderId() != null ? 1 : 0)
                .sum());

        userService.getUserByUsername(username)
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "worker-payment-history";
    }

    @GetMapping("/notifications")
    public String notifications(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();

        model.addAttribute("notifications",      notificationService.getNotificationsFor(username));
        model.addAttribute("unreadNotifications", notificationService.getUnreadCountFor(username));

        userService.getUserByUsername(username)
                   .ifPresent(u -> model.addAttribute("loggedUser", u));

        return "worker-notifications";
    }

    @PostMapping("/notifications/read")
    public String markNotificationRead(@RequestParam Long notificationId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        notificationService.markAsReadFor(notificationId, userDetails.getUsername());
        return "redirect:/worker/notifications";
    }

    @PostMapping("/notifications/read-all")
    public String markAllNotificationsRead(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        notificationService.markAllAsReadFor(userDetails.getUsername());
        return "redirect:/worker/notifications";
    }
}
