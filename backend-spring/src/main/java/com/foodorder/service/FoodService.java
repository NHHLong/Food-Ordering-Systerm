package com.foodorder.service;

import com.foodorder.dto.FoodSaveRequest;
import com.foodorder.model.Food;
import java.util.List;

public interface FoodService {
    List<Food> getAllFoods(String keyword, Integer categoryId);
    Food getFoodById(int foodId);
    Food saveFood(FoodSaveRequest request);
    void deleteFoodById(int foodId);
}
