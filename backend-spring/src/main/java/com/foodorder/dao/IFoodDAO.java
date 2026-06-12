package com.foodorder.dao;

import com.foodorder.model.Food;
import java.util.List;

public interface IFoodDAO extends IGenericDAO<Food, Integer> {
    List<Food> findAllAvailable(String keyword, Integer categoryId);
    void softDeleteById(int foodId);
}
