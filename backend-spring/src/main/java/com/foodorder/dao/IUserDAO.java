package com.foodorder.dao;

import com.foodorder.model.User;

public interface IUserDAO extends IGenericDAO<User, Integer> {
    User findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}
