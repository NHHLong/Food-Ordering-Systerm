package com.foodorder.controller;

import com.foodorder.dto.NotificationCreateRequest;
import com.foodorder.model.Notification;
import com.foodorder.service.NotificationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notifications;

    public NotificationController(NotificationService notifications) {
        this.notifications = notifications;
    }

    @GetMapping
    public List<Notification> list(@RequestParam(required = false) Integer userId) {
        return notifications.getNotifications(userId);
    }

    @PostMapping
    public Notification create(@RequestBody NotificationCreateRequest request) {
        return notifications.createNotification(request);
    }

    @PatchMapping("/{notificationId}/read")
    public Notification markRead(@PathVariable int notificationId) {
        return notifications.markNotificationAsRead(notificationId);
    }
}
