package com.foodorder.dto;

import java.math.BigDecimal;

public record DashboardResponse(BigDecimal revenue, int orderCount) {}
