package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOwnerUsername(String ownerUsername);

    List<Payment> findByWorkerUsername(String workerUsername);

    List<Payment> findByPaymentStatus(String paymentStatus);

    List<Payment> findByOwnerUsernameAndPaymentStatus(String ownerUsername, String paymentStatus);

    List<Payment> findAllByOrderByPaymentIdDesc();
}
