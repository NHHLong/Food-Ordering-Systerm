package com.foodorder.dao;

import com.foodorder.model.Food;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FoodDAOImpl extends GenericDAOImpl<Food, Integer> implements IFoodDAO {
    public FoodDAOImpl() {
        super(Food.class);
    }

    @Override
    public List<Food> findAllAvailable(String keyword, Integer categoryId) {
        StringBuilder jpql = new StringBuilder("""
            SELECT f FROM Food f
            WHERE f.deleted = false
            """);
        var params = new HashMap<String, Object>();
        if (keyword != null && !keyword.isBlank()) {
            jpql.append(" AND LOWER(f.name) LIKE LOWER(:keyword)");
            params.put("keyword", "%" + keyword + "%");
        }
        if (categoryId != null) {
            jpql.append(" AND f.categoryId = :categoryId");
            params.put("categoryId", categoryId);
        }
        jpql.append(" ORDER BY f.foodId");
        List<Food> foods = super.findManyByJPQL(jpql.toString(), params);
        fillCategoryNames(foods);
        return foods;
    }

    @Override
    public List<Food> findAll() {
        List<Food> foods = entityManager
            .createQuery("SELECT f FROM Food f WHERE f.deleted = false ORDER BY f.foodId", Food.class)
            .getResultList();
        fillCategoryNames(foods);
        return foods;
    }

    @Override
    public void softDeleteById(int foodId) {
        findById(foodId).ifPresent(food -> {
            food.setDeleted(true);
            update(food);
        });
    }

    private void fillCategoryNames(List<Food> foods) {
        for (Food food : foods) {
            if (food.getCategoryId() == null) {
                food.setCategoryName("");
                continue;
            }
            String name = entityManager.createQuery(
                    "SELECT c.name FROM Category c WHERE c.categoryId = :categoryId", String.class)
                .setParameter("categoryId", food.getCategoryId())
                .getResultStream()
                .findFirst()
                .orElse("");
            food.setCategoryName(name);
        }
    }
}
