package com.foodorder.dao;

import com.foodorder.model.Notification;
import java.util.List;

public interface INotificationDAO extends IGenericDAO<Notification, Integer> {
    List<Notification> findByUserId(Integer userId);
    Notification markAsRead(int notificationId);
}
