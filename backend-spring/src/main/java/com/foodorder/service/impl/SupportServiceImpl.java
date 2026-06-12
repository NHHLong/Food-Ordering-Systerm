package com.foodorder.service.impl;

import com.foodorder.dao.ISupportDAO;
import com.foodorder.dto.NotificationCreateRequest;
import com.foodorder.dto.SupportCreateRequest;
import com.foodorder.model.SupportRequest;
import com.foodorder.service.SupportService;
import com.foodorder.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupportServiceImpl implements SupportService {
    private final ISupportDAO supportDAO;
    private final NotificationService notificationService;

    public SupportServiceImpl(ISupportDAO supportDAO, NotificationService notificationService) {
        this.supportDAO = supportDAO;
        this.notificationService = notificationService;
    }

    @Override
    public List<SupportRequest> getAllSupportRequests() {
        return supportDAO.findAllWithUsernames();
    }

    @Override
    @Transactional
    public SupportRequest createSupportRequest(SupportCreateRequest request) {
        if (request.subject() == null || request.subject().isBlank()
            || request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("Subject and message are required.");
        }
        SupportRequest support = new SupportRequest();
        support.setUserId(request.userId());
        support.setSubject(request.subject());
        support.setMessage(request.message());
        support.setStatus("Open");
        support.setCreatedAt(LocalDateTime.now());
        SupportRequest saved = supportDAO.save(support);
        notificationService.createNotification(new NotificationCreateRequest(
            request.userId(),
            "Support request sent",
            "Your support request #" + saved.getSupportRequestId() + " has been sent."
        ));
        notificationService.createNotification(new NotificationCreateRequest(
            null,
            "New support request",
            "A new support request was created."
        ));
        return saved;
    }
}
