package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByOrderByCreatedAtDesc();

    List<Notification> findByReadStatus(boolean readStatus);

    List<Notification> findByRecipientOrderByCreatedAtDesc(String recipient);

    List<Notification> findByRecipientAndReadStatusFalse(String recipient);

    long countByReadStatus(boolean readStatus);

    long countByRecipientAndReadStatusFalse(String recipient);
}
