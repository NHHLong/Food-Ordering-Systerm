package com.foodorder.service;

import com.foodorder.dto.RegisterRequest;
import com.foodorder.dto.UserActiveRequest;
import com.foodorder.dto.UserRoleRequest;
import com.foodorder.model.User;
import java.util.List;

public interface UserService {
    User login(String username, String password);
    int register(RegisterRequest request);
    List<User> getAllUsers();
    User updateUserActive(int userId, UserActiveRequest request);
    User updateUserRole(int userId, UserRoleRequest request);
}
