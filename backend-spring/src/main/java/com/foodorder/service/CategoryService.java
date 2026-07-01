package com.foodorder.service;

import com.foodorder.dto.CategoryActiveRequest;
import com.foodorder.dto.CategorySaveRequest;
import com.foodorder.model.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(boolean onlyActive);
    Category saveCategory(CategorySaveRequest request);
    Category updateCategoryActive(int categoryId, CategoryActiveRequest request);
}
