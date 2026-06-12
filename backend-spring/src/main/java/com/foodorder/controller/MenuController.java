package com.foodorder.controller;

import com.foodorder.model.Category;
import com.foodorder.model.Food;
import com.foodorder.service.CategoryService;
import com.foodorder.service.FoodService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final FoodService foods;
    private final CategoryService categories;

    public MenuController(FoodService foods, CategoryService categories) {
        this.foods = foods;
        this.categories = categories;
    }

    @GetMapping
    public Map<String, Object> index(@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer categoryId) {
        List<Category> activeCategories = categories.getAllCategories(true);
        List<Food> filteredFoods = foods.getAllFoods(keyword, categoryId);
        return Map.of("foods", filteredFoods, "categories", activeCategories);
    }
}
