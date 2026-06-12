package com.foodorder.service.impl;

import com.foodorder.dao.INotificationDAO;
import com.foodorder.dto.NotificationCreateRequest;
import com.foodorder.model.Notification;
import com.foodorder.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final INotificationDAO notificationDAO;

    public NotificationServiceImpl(INotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    @Override
    @Transactional
    public Notification createNotification(NotificationCreateRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.userId());
        notification.setTitle(request.title());
        notification.setMessage(request.message());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationDAO.save(notification);
    }

    @Override
    public List<Notification> getNotifications(Integer userId) {
        return notificationDAO.findByUserId(userId);
    }

    @Override
    @Transactional
    public Notification markNotificationAsRead(int notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }
}
