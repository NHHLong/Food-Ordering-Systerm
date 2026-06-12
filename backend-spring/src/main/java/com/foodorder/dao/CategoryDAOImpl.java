package com.foodorder.dao;

import com.foodorder.model.Category;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAOImpl extends GenericDAOImpl<Category, Integer> implements ICategoryDAO {
    public CategoryDAOImpl() {
        super(Category.class);
    }

    @Override
    public List<Category> findAllActive() {
        return super.findManyByJPQL("SELECT c FROM Category c WHERE c.active = true ORDER BY c.name", new HashMap<>());
    }

    @Override
    public List<Category> findAll() {
        return entityManager.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class).getResultList();
    }
}
