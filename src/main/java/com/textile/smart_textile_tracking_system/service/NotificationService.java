package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.Notification;
import com.textile.smart_textile_tracking_system.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Creates a notification. Never throws so it can never break a business flow.
     */
    public void createNotification(String title, String message, String recipient) {
        try {
            notificationRepository.save(new Notification(title, message, recipient));
        } catch (Exception ignored) {
            // Notification creation must not interrupt the main operation.
        }
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Notification> getNotificationsFor(String recipient) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(recipient);
    }

    public long getUnreadCount() {
        return notificationRepository.countByReadStatus(false);
    }

    /** Unread notifications for one recipient, counted in the database. */
    public long getUnreadCountFor(String recipient) {
        return notificationRepository.countByRecipientAndReadStatusFalse(recipient);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        });
    }

    /** Marks one notification read, but only when it belongs to the given recipient. */
    public void markAsReadFor(Long notificationId, String recipient) {
        if (recipient == null) return;
        notificationRepository.findById(notificationId)
                .filter(notification -> recipient.equals(notification.getRecipient()))
                .ifPresent(notification -> {
                    notification.setReadStatus(true);
                    notificationRepository.save(notification);
                });
    }

    public void markAllAsRead() {
        List<Notification> unread = notificationRepository.findByReadStatus(false);
        unread.forEach(n -> n.setReadStatus(true));
        notificationRepository.saveAll(unread);
    }

    /** Marks every unread notification of one recipient as read, leaving other users untouched. */
    public void markAllAsReadFor(String recipient) {
        if (recipient == null) return;
        List<Notification> unread = notificationRepository.findByRecipientAndReadStatusFalse(recipient);
        unread.forEach(n -> n.setReadStatus(true));
        notificationRepository.saveAll(unread);
    }
}
