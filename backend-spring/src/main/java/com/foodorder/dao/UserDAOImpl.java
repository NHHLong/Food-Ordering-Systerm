package com.foodorder.dao;

import com.foodorder.model.User;
import java.util.HashMap;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User, Integer> implements IUserDAO {
    public UserDAOImpl() {
        super(User.class);
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String jpql = "SELECT u FROM UserEntity u WHERE u.username = :username AND u.password = :password AND u.active = true";
        var params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        return super.findSingleByJPQL(jpql, params);
    }

    @Override
    public boolean existsByUsername(String username) {
        String jpql = "SELECT COUNT(u) FROM UserEntity u WHERE u.username = :username";
        Long count = entityManager.createQuery(jpql, Long.class)
            .setParameter("username", username)
            .getSingleResult();
        return count != null && count > 0;
    }
}
