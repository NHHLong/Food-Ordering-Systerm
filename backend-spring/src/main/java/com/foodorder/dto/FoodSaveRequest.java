package com.foodorder.dto;

import java.math.BigDecimal;

public record FoodSaveRequest(
    int foodId,
    String name,
    Integer categoryId,
    String description,
    BigDecimal price,
    String image,
    String status
) {}
