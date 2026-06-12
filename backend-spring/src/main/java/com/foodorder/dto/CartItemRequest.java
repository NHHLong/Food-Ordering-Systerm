package com.foodorder.dto;

public record CartItemRequest(int userId, int foodId, int quantity) {}
