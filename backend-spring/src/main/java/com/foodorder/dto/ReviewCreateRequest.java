package com.foodorder.dto;

public record ReviewCreateRequest(int userId, int foodId, int rating, String comment) {}
