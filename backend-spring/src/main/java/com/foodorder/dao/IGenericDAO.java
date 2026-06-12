package com.foodorder.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGenericDAO<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    T update(T entity);
    void delete(T entity);
    void deleteById(ID id);
    List<T> findManyByJPQL(String jpql, Map<String, Object> params);
    T findSingleByJPQL(String jpql, Map<String, Object> params);
    long countAll();
}
