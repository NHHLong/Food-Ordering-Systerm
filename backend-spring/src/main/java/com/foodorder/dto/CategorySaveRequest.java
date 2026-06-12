package com.foodorder.dto;

public record CategorySaveRequest(int categoryId, String name, String description, boolean active) {}
