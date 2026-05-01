package com.cleancity.backend.controller;

import com.cleancity.backend.entity.User;
import com.cleancity.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ REGISTER USER
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user); // make sure this exists in service
    }

    // ✅ LOGIN USER (returns JWT token)
    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody User user) {

        String token = userService.loginUser(user.getEmail(), user.getPassword());

        User existingUser = userService.getUserByEmail(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", existingUser.getRole());

        return response;
    }
}