package com.foodorder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VoucherSaveRequest(
    int voucherId,
    String code,
    String description,
    Integer discountPercent,
    BigDecimal discountAmount,
    int minimumOrderValue,
    int maxUsage,
    int usedCount,
    LocalDateTime startDate,
    LocalDateTime expiryDate,
    boolean active
) {}
