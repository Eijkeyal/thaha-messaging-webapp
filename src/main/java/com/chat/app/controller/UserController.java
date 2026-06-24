package com.chat.app.controller;

import com.chat.app.entity.User;
import com.chat.app.repository.UserRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public Map<String,String> getCurrentUser(){
        String userId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();
        User user =
                userRepository.findById(userId)
                        .orElseThrow();
        return Map.of(

                "id", user.getUserId(),

                "name", user.getUsername(),

                "email", user.getEmail()

        );

    }
    @GetMapping
    public List<Map<String,String>> getAllUsers() {

        String currentUserId =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository.findAll()
                .stream()
                .filter(user ->
                        !user.getUserId().equals(currentUserId))
                .map(user -> Map.of(
                        "userId", user.getUserId(),
                        "username", user.getUsername(),
                        "email", user.getEmail()
                ))
                .toList();
    }
}