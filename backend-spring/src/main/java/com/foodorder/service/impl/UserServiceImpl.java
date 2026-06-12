package com.foodorder.service.impl;

import com.foodorder.dao.IUserDAO;
import com.foodorder.dto.RegisterRequest;
import com.foodorder.dto.UserActiveRequest;
import com.foodorder.dto.UserRoleRequest;
import com.foodorder.model.User;
import com.foodorder.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final IUserDAO userDAO;

    public UserServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Please enter username and password.");
        }
        return userDAO.findByUsernameAndPassword(username.trim(), password);
    }

    @Override
    @Transactional
    public int register(RegisterRequest request) {
        if (request.username() == null || request.username().isBlank() || request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Username and password are required.");
        }
        String username = request.username().trim();
        if (userDAO.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(request.password());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setRole("Customer");
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        return userDAO.save(user).getUserId();
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    @Transactional
    public User updateUserActive(int userId, UserActiveRequest request) {
        User user = userDAO.findById(userId).orElseThrow(() -> new IllegalArgumentException("User does not exist."));
        user.setActive(request.active());
        return userDAO.update(user);
    }

    @Override
    @Transactional
    public User updateUserRole(int userId, UserRoleRequest request) {
        User user = userDAO.findById(userId).orElseThrow(() -> new IllegalArgumentException("User does not exist."));
        String role = request.role() == null ? "" : request.role().trim();
        if (!List.of("Customer", "Staff", "Admin").contains(role)) {
            throw new IllegalArgumentException("Role must be Customer, Staff or Admin.");
        }
        user.setRole(role);
        return userDAO.update(user);
    }
}
