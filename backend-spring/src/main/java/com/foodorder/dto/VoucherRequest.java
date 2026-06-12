package com.foodorder.dto;

import java.math.BigDecimal;

public record VoucherRequest(String code, BigDecimal orderTotal) {}
