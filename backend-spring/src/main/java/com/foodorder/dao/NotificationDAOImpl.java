package com.foodorder.dao;

import com.foodorder.model.Notification;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationDAOImpl extends GenericDAOImpl<Notification, Integer> implements INotificationDAO {
    public NotificationDAOImpl() {
        super(Notification.class);
    }

    @Override
    public List<Notification> findByUserId(Integer userId) {
        String jpql = userId == null
            ? "SELECT n FROM Notification n ORDER BY n.createdAt DESC"
            : "SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.createdAt DESC";
        var query = entityManager.createQuery(jpql, Notification.class);
        if (userId != null) query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Notification markAsRead(int notificationId) {
        Notification notification = findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException("Notification does not exist."));
        notification.setRead(true);
        return update(notification);
    }
}
