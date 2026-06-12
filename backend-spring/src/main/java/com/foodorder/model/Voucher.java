package com.foodorder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VoucherId")
    private int voucherId;
    @Column(name = "Code", nullable = false)
    private String code = "";
    @Column(name = "Description")
    private String description;
    @Column(name = "DiscountPercent")
    private Integer discountPercent;
    @Column(name = "DiscountAmount")
    private BigDecimal discountAmount;
    @Column(name = "MinimumOrderValue")
    private int minimumOrderValue;
    @Column(name = "MaxUsage")
    private int maxUsage;
    @Column(name = "UsedCount")
    private int usedCount;
    @Column(name = "StartDate")
    private LocalDateTime startDate;
    @Column(name = "ExpiryDate")
    private LocalDateTime expiryDate;
    @Column(name = "IsActive")
    private boolean active;

    public int getVoucherId() { return voucherId; }
    public void setVoucherId(int voucherId) { this.voucherId = voucherId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public int getMinimumOrderValue() { return minimumOrderValue; }
    public void setMinimumOrderValue(int minimumOrderValue) { this.minimumOrderValue = minimumOrderValue; }
    public int getMaxUsage() { return maxUsage; }
    public void setMaxUsage(int maxUsage) { this.maxUsage = maxUsage; }
    public int getUsedCount() { return usedCount; }
    public void setUsedCount(int usedCount) { this.usedCount = usedCount; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
