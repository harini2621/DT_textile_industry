package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.Order;
import com.textile.smart_textile_tracking_system.entity.ProductionOrder;
import com.textile.smart_textile_tracking_system.entity.Worker;
import com.textile.smart_textile_tracking_system.entity.Payment;
import com.textile.smart_textile_tracking_system.service.ActivityLogService;
import com.textile.smart_textile_tracking_system.service.OrderService;
import com.textile.smart_textile_tracking_system.service.PaymentService;
import com.textile.smart_textile_tracking_system.service.ProductionOrderService;
import com.textile.smart_textile_tracking_system.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired private WorkerService workerService;
    @Autowired private OrderService orderService;
    @Autowired private ProductionOrderService productionOrderService;
    @Autowired private PaymentService paymentService;
    @Autowired private ActivityLogService activityLogService;

    /** Process stages used by Process Stage Tracking, in order. */
    private static final List<String> PROCESS_STAGES = List.of(
            "Cutting", "Stitching", "Dyeing", "Finishing", "Packing", "Completed");

    // ─── Add Worker ───────────────────────────────────────────────────────────

    @GetMapping("/add-worker")
    public String addWorkerPage(Model model) {
        model.addAttribute("worker", new Worker());
        return "owner-add-worker";
    }

    @PostMapping("/add-worker")
    public String saveWorker(@ModelAttribute Worker worker,
                             RedirectAttributes redirectAttributes) {
        if (worker.getWorkerName() == null || worker.getWorkerName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Worker name is required.");
            return "redirect:/owner/add-worker";
        }
        if (worker.getAvailability() == null || worker.getAvailability().isBlank()) {
            worker.setAvailability("Available");
        }
        workerService.saveWorker(worker);
        redirectAttributes.addFlashAttribute("success", "Worker added successfully.");
        return "redirect:/workers";
    }

                    // ─── Assign Worker to Order ───────────────────────────────────────────────

    @GetMapping("/assign-worker")
    public String assignWorkerPage(@RequestParam(required = false) Long batchId,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {
        if (batchId == null) {
            return "redirect:/owner/production-tracking";
        }
        model.addAttribute("batch", productionOrderService.getProductionOrderById(batchId).orElse(null));
        model.addAttribute("workers", workerService.getWorkersByAvailability("Available"));
        return "owner-assign-worker";
    }

    @PostMapping("/assign-worker")
    public String doAssignWorker(@RequestParam Long orderId,
                                 @RequestParam String workerUsername,
                                 RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/owner/assign-worker";
        }
        order.setWorkerUsername(workerUsername);
        orderService.saveOrder(order);
        redirectAttributes.addFlashAttribute("success", "Worker assigned successfully.");
        return "redirect:/owner-orders";
    }

    // ─── Production Batch ─────────────────────────────────────────────────────

    @GetMapping("/production-batch")
    public String productionBatchPage(Model model) {
        model.addAttribute("batch", new ProductionOrder());
        return "owner-production-batch";
    }

    @PostMapping("/production-batch")
    public String saveProductionBatch(@ModelAttribute ProductionOrder batch,
                                      RedirectAttributes redirectAttributes) {
        if (batch.getProductName() == null || batch.getProductName().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Product name is required.");
            return "redirect:/owner/production-batch";
        }
        if (batch.getOrderNumber() == null || batch.getOrderNumber().isBlank()) {
            batch.setOrderNumber("BATCH-" + System.currentTimeMillis());
        }
        if (batch.getStatus() == null || batch.getStatus().isBlank()) {
            batch.setStatus("Pending");
        }
        productionOrderService.saveProductionOrder(batch);
        redirectAttributes.addFlashAttribute("success", "Production batch created successfully.");
        return "redirect:/owner/production-tracking";
    }

    // ─── Production Tracking ──────────────────────────────────────────────────

    @GetMapping("/production-tracking")
    public String productionTracking(Model model) {
        List<ProductionOrder> all = productionOrderService.getAllProductionOrders();
        model.addAttribute("productionOrders", all);
        model.addAttribute("totalBatches",      all.size());
        model.addAttribute("pendingBatches",    productionOrderService.getOrdersByStatus("Pending").size());
        model.addAttribute("inProgressBatches", productionOrderService.getOrdersByStatus("In Progress").size());
        model.addAttribute("completedBatches",  productionOrderService.getOrdersByStatus("Completed").size());
        model.addAttribute("processStages",     PROCESS_STAGES);
        model.addAttribute("workers",           workerService.getAllWorkers());
        return "owner-production-tracking";
    }

    // ─── Process Stage Tracking ───────────────────────────────

    @PostMapping("/production-tracking/update-stage")
    public String updateBatchStage(@RequestParam Long batchId,
                                   @RequestParam String stage,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        productionOrderService.getProductionOrderById(batchId).ifPresent(batch -> {
            batch.setCurrentStage(stage);
            // Keep the high-level status consistent with the stage.
            if ("Completed".equalsIgnoreCase(stage)) {
                batch.setStatus("Completed");
            } else if (batch.getStatus() == null || "Pending".equalsIgnoreCase(batch.getStatus())) {
                batch.setStatus("In Progress");
            }
            productionOrderService.saveProductionOrder(batch);
        });
        activityLogService.log("PROCESS_STAGE_UPDATE", userDetails.getUsername(),
                "Batch " + batchId + " moved to stage '" + stage + "'");
        redirectAttributes.addFlashAttribute("success", "Process stage updated.");
        return "redirect:/owner/production-tracking";
    }

    @PostMapping("/production-tracking/assign-worker")
    public String assignWorkerToBatch(@RequestParam Long batchId,
                                      @RequestParam String workerUsername,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      RedirectAttributes redirectAttributes) {
        productionOrderService.getProductionOrderById(batchId).ifPresent(batch -> {
            batch.setAssignedWorker(workerUsername);
            productionOrderService.saveProductionOrder(batch);
        });
        activityLogService.log("WORKER_ASSIGNMENT", userDetails.getUsername(),
                "Worker '" + workerUsername + "' assigned to batch " + batchId);
        redirectAttributes.addFlashAttribute("success", "Worker assigned to batch.");
        return "redirect:/owner/production-tracking";
    }

    @PostMapping("/production-tracking/update-status")
    public String updateBatchStatus(@RequestParam Long batchId,
                                    @RequestParam String status,
                                    RedirectAttributes redirectAttributes) {
        productionOrderService.getProductionOrderById(batchId).ifPresent(batch -> {
            batch.setStatus(status);
            productionOrderService.saveProductionOrder(batch);
        });
        redirectAttributes.addFlashAttribute("success", "Batch status updated.");
        return "redirect:/owner/production-tracking";
    }

    // ─── Payment Tracking ─────────────────────────────────────────────────────

    @GetMapping("/payment-tracking")
    public String paymentTracking(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        String username = userDetails.getUsername();
        List<Order> orders = orderService.getOwnerOrders(username);
        long paid    = orders.stream().filter(o -> "Completed".equalsIgnoreCase(o.getStatus())).count();
        long pending = orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count();
        long inProg  = orders.stream().filter(o -> "IN PROGRESS".equalsIgnoreCase(o.getStatus())).count();
        model.addAttribute("orders",         orders);
        model.addAttribute("totalOrders",    orders.size());
        model.addAttribute("paidOrders",     paid);
        model.addAttribute("pendingPayment", pending);
        model.addAttribute("inProgress",     inProg);

        // Real payment records from the payments table.
        List<Payment> ownerPayments = paymentService.getOwnerPayments(username);
        model.addAttribute("payments",       ownerPayments);
        model.addAttribute("totalPaid",
                paymentService.getTotalAmount(paymentService.getOwnerPaymentsByStatus(username, "Paid")));
        model.addAttribute("totalUnpaid",
                paymentService.getTotalAmount(paymentService.getOwnerPaymentsByStatus(username, "Pending")));
        return "owner-payment-tracking";
    }

    @PostMapping("/payment-tracking/create")
    public String createPayment(@RequestParam Long orderId,
                                @RequestParam(required = false) String workerUsername,
                                @RequestParam double amount,
                                @RequestParam(required = false) String remarks,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        Payment payment = new Payment(userDetails.getUsername(), workerUsername, amount, "Pending");
        payment.setOrderId(orderId);
        payment.setRemarks(remarks);
        paymentService.savePayment(payment);
        activityLogService.log("PAYMENT_CREATED", userDetails.getUsername(),
                "Payment of " + amount + " created for order " + orderId);
        redirectAttributes.addFlashAttribute("success", "Payment recorded successfully.");
        return "redirect:/owner/payment-tracking";
    }

    @PostMapping("/payment-tracking/update-status")
    public String updatePaymentStatus(@RequestParam Long paymentId,
                                      @RequestParam String paymentStatus,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      RedirectAttributes redirectAttributes) {
        paymentService.updatePaymentStatus(paymentId, paymentStatus);
        activityLogService.log("PAYMENT_STATUS_UPDATE", userDetails.getUsername(),
                "Payment " + paymentId + " marked as " + paymentStatus);
        redirectAttributes.addFlashAttribute("success", "Payment status updated.");
        return "redirect:/owner/payment-tracking";
    }
}
