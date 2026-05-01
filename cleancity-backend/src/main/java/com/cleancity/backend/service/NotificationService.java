package com.cleancity.backend.service;

import com.cleancity.backend.entity.Notification;
import com.cleancity.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // ✅ Create notification
    public Notification createNotification(String email, String title, String message) {

        Notification notification = new Notification();
        notification.setEmail(email);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setReadStatus(false);

        return notificationRepository.save(notification);
    }

    // ✅ Get notifications (latest first)
    public List<Notification> getUserNotifications(String email) {

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        return notificationRepository.findByEmailOrderByCreatedAtDesc(email);
    }

    // ✅ Mark as read
    public Notification markAsRead(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.isReadStatus()) {
            notification.setReadStatus(true);
        }

        return notificationRepository.save(notification);
    }
}