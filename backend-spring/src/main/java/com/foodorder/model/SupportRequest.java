package com.foodorder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;

@Entity
@Table(name = "SupportRequests")
public class SupportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SupportRequestId")
    private int supportRequestId;
    @Column(name = "UserId", nullable = false)
    private int userId;
    @Transient
    private String username = "";
    @Column(name = "Subject", nullable = false)
    private String subject = "";
    @Column(name = "Message", nullable = false)
    private String message = "";
    @Column(name = "Status")
    private String status = "Open";
    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    public int getSupportRequestId() { return supportRequestId; }
    public void setSupportRequestId(int supportRequestId) { this.supportRequestId = supportRequestId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
