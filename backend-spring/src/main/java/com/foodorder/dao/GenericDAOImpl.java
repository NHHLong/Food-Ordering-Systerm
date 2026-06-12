package com.foodorder.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class GenericDAOImpl<T, ID> implements IGenericDAO<T, ID> {
    private final Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<T> findAll() {
        String jpql = "SELECT e FROM " + entityName() + " e";
        return entityManager.createQuery(jpql, entityClass).getResultList();
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public List<T> findManyByJPQL(String jpql, Map<String, Object> params) {
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public T findSingleByJPQL(String jpql, Map<String, Object> params) {
        List<T> rows = findManyByJPQL(jpql, params);
        return rows.isEmpty() ? null : rows.get(0);
    }

    @Override
    public long countAll() {
        String jpql = "SELECT COUNT(e) FROM " + entityName() + " e";
        return entityManager.createQuery(jpql, Long.class).getSingleResult();
    }

    protected String entityName() {
        return entityManager.getMetamodel().entity(entityClass).getName();
    }
}
