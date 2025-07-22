package com.springbootplayground.crudmongo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbootplayground.crudmongo.dto.RegisterRequest;
import com.springbootplayground.crudmongo.model.User;
import com.springbootplayground.crudmongo.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest req) {

        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        if (req.getRole() == null || (!req.getRole().equals("user") && !req.getRole().equals("admin"))) {
            throw new RuntimeException("Invalid role. Must be 'user' or 'admin'");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();

        this.userRepo.save(user);
    }
}
