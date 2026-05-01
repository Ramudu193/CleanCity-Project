package com.cleancity.backend.controller;

import com.cleancity.backend.entity.Notification;
import com.cleancity.backend.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{email}")
    public List<Notification> getNotifications(@PathVariable String email) {
        return notificationService.getUserNotifications(email);
    }

    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "Notification marked as read";
    }
}