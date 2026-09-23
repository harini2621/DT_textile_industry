package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Payment;
import com.textile.smart_textile_tracking_system.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByPaymentIdDesc();
    }

    public List<Payment> getOwnerPayments(String ownerUsername) {
        return paymentRepository.findByOwnerUsername(ownerUsername);
    }

    public List<Payment> getWorkerPayments(String workerUsername) {
        return paymentRepository.findByWorkerUsername(workerUsername);
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    public List<Payment> getOwnerPaymentsByStatus(String ownerUsername, String status) {
        return paymentRepository.findByOwnerUsernameAndPaymentStatus(ownerUsername, status);
    }

    public double getTotalAmount(List<Payment> payments) {
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public void updatePaymentStatus(Long id, String status) {
        Payment payment = getPaymentById(id);
        if (payment != null) {
            payment.setPaymentStatus(status);
            paymentRepository.save(payment);
        }
    }
}
