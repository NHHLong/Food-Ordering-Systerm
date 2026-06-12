package com.foodorder.controller;

import com.foodorder.model.Food;
import com.foodorder.service.FoodService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    private final FoodService foods;

    public FoodController(FoodService foods) {
        this.foods = foods;
    }

    @GetMapping
    public List<Food> getAll(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer categoryId) {
        return foods.getAllFoods(keyword, categoryId);
    }

    @GetMapping("/{foodId}")
    public Food getById(@PathVariable int foodId) {
        return foods.getFoodById(foodId);
    }
}
