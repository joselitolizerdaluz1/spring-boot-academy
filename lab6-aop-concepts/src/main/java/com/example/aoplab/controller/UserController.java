package com.example.aoplab.controller;

import com.example.aoplab.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserService.User> createUser(@RequestBody CreateUserRequest request) {
        UserService.User user = userService.createUser(request.username, request.fullName, request.email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserService.User> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserService.User> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        try {
            UserService.User user = userService.updateUser(id, request.fullName, request.email);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<UserService.User>> getAllUsers() {
        List<UserService.User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<UserService.User> getUserByUsername(@PathVariable String username) {
        try {
            UserService.User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/simulate-error")
    public ResponseEntity<String> simulateError() {
        userService.simulateError();
        return ResponseEntity.ok("This should not be reached");
    }
    
    // Request DTOs
    public static class CreateUserRequest {
        public String username;
        public String fullName;
        public String email;
    }
    
    public static class UpdateUserRequest {
        public String fullName;
        public String email;
    }
}
