package com.springbootplayground.crudmongo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springbootplayground.crudmongo.dto.UserDTO;
import com.springbootplayground.crudmongo.model.User;
import com.springbootplayground.crudmongo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> getAllUsers(Authentication auth) {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        try {
            User user = userService.getById(id);
            return ResponseEntity.ok(user);

        } catch (RuntimeException r) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + r.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());

        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable String id, @Valid @RequestBody UserDTO user) {
        try {
            User updatedUser = userService.update(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException r) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + r.getMessage());
        } catch (Exception e) {
            System.err.println("UserController: Error updating user - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException r) {
            System.err.println("UserController: User not found - " + r.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + r.getMessage());
        } catch (Exception e) {
            System.err.println("UserController: Error deleting user - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
