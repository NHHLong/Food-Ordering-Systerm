package com.foodorder.service.impl;

import com.foodorder.dao.ISupportDAO;
import com.foodorder.dto.NotificationCreateRequest;
import com.foodorder.dto.SupportCreateRequest;
import com.foodorder.dto.SupportStatusRequest;
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

    @Override
    @Transactional
    public SupportRequest updateSupportStatus(int supportRequestId, SupportStatusRequest request) {
        String status = request.status() == null ? "" : request.status().trim();
        if (!List.of("Open", "InProgress", "Resolved").contains(status)) {
            throw new IllegalArgumentException("Status must be Open, InProgress or Resolved.");
        }
        SupportRequest support = supportDAO.findById(supportRequestId)
            .orElseThrow(() -> new IllegalArgumentException("Support request does not exist."));
        support.setStatus(status);
        SupportRequest updated = supportDAO.update(support);
        notificationService.createNotification(new NotificationCreateRequest(
            updated.getUserId(),
            "Support request updated",
            "Your support request #" + supportRequestId + " status changed to " + status + "."
        ));
        return updated;
    }
}
