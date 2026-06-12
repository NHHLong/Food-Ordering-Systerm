package com.foodorder.service.impl;

import com.foodorder.dao.IFoodDAO;
import com.foodorder.dto.FoodSaveRequest;
import com.foodorder.model.Food;
import com.foodorder.service.FoodService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FoodServiceImpl implements FoodService {
    private final IFoodDAO foodDAO;

    public FoodServiceImpl(IFoodDAO foodDAO) {
        this.foodDAO = foodDAO;
    }

    @Override
    public List<Food> getAllFoods(String keyword, Integer categoryId) {
        return foodDAO.findAllAvailable(keyword, categoryId);
    }

    @Override
    public Food getFoodById(int foodId) {
        return foodDAO.findById(foodId).orElse(null);
    }

    @Override
    @Transactional
    public Food saveFood(FoodSaveRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Food name is required.");
        }
        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        Food food = request.foodId() > 0
            ? foodDAO.findById(request.foodId()).orElse(new Food())
            : new Food();
        food.setName(request.name());
        food.setPrice(request.price());
        food.setStatus(request.status() == null || request.status().isBlank() ? "Available" : request.status());
        food.setImage(request.image());
        food.setCategoryId(request.categoryId());
        food.setDescription(request.description());
        if (food.getCreatedAt() == null) {
            food.setCreatedAt(LocalDateTime.now());
        }
        return request.foodId() > 0 ? foodDAO.update(food) : foodDAO.save(food);
    }

    @Override
    @Transactional
    public void deleteFoodById(int foodId) {
        foodDAO.softDeleteById(foodId);
    }
}
