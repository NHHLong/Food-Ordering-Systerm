package com.foodorder.dao;

import com.foodorder.model.Category;
import java.util.List;

public interface ICategoryDAO extends IGenericDAO<Category, Integer> {
    List<Category> findAllActive();
}
