package com.foodorder.service.impl;

import com.foodorder.dao.ICategoryDAO;
import com.foodorder.dto.CategoryActiveRequest;
import com.foodorder.dto.CategorySaveRequest;
import com.foodorder.model.Category;
import com.foodorder.service.CategoryService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ICategoryDAO categoryDAO;

    public CategoryServiceImpl(ICategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> getAllCategories(boolean onlyActive) {
        return onlyActive ? categoryDAO.findAllActive() : categoryDAO.findAll();
    }

    @Override
    @Transactional
    public Category saveCategory(CategorySaveRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        Category category = request.categoryId() > 0
            ? categoryDAO.findById(request.categoryId()).orElse(new Category())
            : new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        category.setActive(request.active());
        return request.categoryId() > 0 ? categoryDAO.update(category) : categoryDAO.save(category);
    }

    @Override
    @Transactional
    public Category updateCategoryActive(int categoryId, CategoryActiveRequest request) {
        Category category = categoryDAO.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category does not exist."));
        category.setActive(request.active());
        return categoryDAO.update(category);
    }
}
