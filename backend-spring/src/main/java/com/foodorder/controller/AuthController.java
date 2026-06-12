package com.foodorder.controller;

import com.foodorder.dto.LoginRequest;
import com.foodorder.dto.RegisterRequest;
import com.foodorder.model.User;
import com.foodorder.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService users;

    public AuthController(UserService users) {
        this.users = users;
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        User user = users.login(request.username(), request.password());
        if (user == null) {
            throw new IllegalArgumentException("Wrong username or password.");
        }
        return user;
    }

    @PostMapping("/register")
    public int register(@RequestBody RegisterRequest request) {
        return users.register(request);
    }
}
