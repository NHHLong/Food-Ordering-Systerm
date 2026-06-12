package com.foodorder.service;

import com.foodorder.dto.NotificationCreateRequest;
import com.foodorder.model.Notification;
import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationCreateRequest request);
    List<Notification> getNotifications(Integer userId);
    Notification markNotificationAsRead(int notificationId);
}
