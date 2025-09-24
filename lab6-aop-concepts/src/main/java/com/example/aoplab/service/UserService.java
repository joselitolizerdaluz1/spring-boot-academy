package com.example.aoplab.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final List<User> users = new ArrayList<>();
    
    public UserService() {
        // Initialize with some sample users
        users.add(new User(1L, "john.doe", "John Doe", "john@example.com"));
        users.add(new User(2L, "jane.smith", "Jane Smith", "jane@example.com"));
        users.add(new User(3L, "bob.wilson", "Bob Wilson", "bob@example.com"));
    }
    
    public User createUser(String username, String fullName, String email) {
        // TODO 2: This method will be advised by @Before aspect
        User user = new User((long) (users.size() + 1), username, fullName, email);
        users.add(user);
        return user;
    }
    
    public Optional<User> findUserById(Long id) {
        // TODO 3: This method will be advised by @AfterReturning aspect
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    public User updateUser(Long id, String fullName, String email) {
        // TODO 4: This method will be advised by @Around aspect
        User user = findUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setFullName(fullName);
        user.setEmail(email);
        return user;
    }
    
    public void deleteUser(Long id) {
        // TODO 5: This method will be advised by @AfterThrowing aspect
        User user = findUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        users.remove(user);
    }
    
    public List<User> getAllUsers() {
        // TODO 6: This method will be advised by @After aspect
        return new ArrayList<>(users);
    }
    
    public User getUserByUsername(String username) {
        // TODO 7: This method will demonstrate pointcut expressions
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
    
    public void simulateError() {
        // TODO 8: This method will be used to demonstrate @AfterThrowing
        throw new RuntimeException("Simulated error for demonstration");
    }
    
    public static class User {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        
        public User(Long id, String username, String fullName, String email) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", username='" + username + "', fullName='" + fullName + "', email='" + email + "'}";
        }
    }
}
