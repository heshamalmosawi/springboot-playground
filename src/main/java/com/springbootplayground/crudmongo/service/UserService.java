package com.springbootplayground.crudmongo.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springbootplayground.crudmongo.dto.LoginRequest;
import com.springbootplayground.crudmongo.dto.RegisterRequest;
import com.springbootplayground.crudmongo.dto.UserDTO;
import com.springbootplayground.crudmongo.model.User;
import com.springbootplayground.crudmongo.repository.UserRepository;
import com.springbootplayground.crudmongo.service.security.jwtService;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest req) {
        if (req.getName() == null || req.getName().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (req.getEmail() == null || req.getEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (req.getPassword() == null || req.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        if (req.getRole() == null || req.getRole().isEmpty()) {
            throw new RuntimeException("Role is required");
        }
        if (!req.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) {
            throw new RuntimeException("Invalid email format");
        }
        if (req.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }

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

    public String loginUser(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .peek(user -> user.setPassword(null)) // hide password
                .toList();
    }

    public User getById(String id) {
        return userRepo.findById(id)
                .map(user -> {
                    user.setPassword(null);
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(String id, UserDTO user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Invalid or missing email");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required for full update");
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User updatedUser = userRepo.save(existingUser);
        updatedUser.setPassword(null);
        return updatedUser;
    }

    public void deleteUser(String id) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(existingUser);
    }
}
