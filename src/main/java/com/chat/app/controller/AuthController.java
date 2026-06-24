package com.chat.app.controller;

import com.chat.app.dto.LoginRequest;
import com.chat.app.dto.RegisterRequest;
import com.chat.app.entity.User;
import com.chat.app.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.chat.app.security.JwtService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @GetMapping("/test")
    public String test() {
        return "Auth Controller Working";
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username already exists";
        }

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // For learning only. Later use BCrypt.
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        return "User registered successfully";
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ){

        User user =
                userRepository.findByEmail(request.getEmail());


        if(user == null){

            return ResponseEntity
                    .status(401)
                    .body("Invalid credentials");

        }

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )){

            return ResponseEntity
                    .status(401)
                    .body("Invalid credentials");

        }

        String token =
                jwtService.generateToken(
                        user.getUserId()
                );


        return ResponseEntity.ok(token);

    }
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}