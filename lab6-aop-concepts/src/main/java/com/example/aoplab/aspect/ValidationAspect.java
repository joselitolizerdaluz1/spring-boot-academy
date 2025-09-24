package com.example.aoplab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * This aspect demonstrates input validation using AOP
 * It shows how aspects can be used for cross-cutting validation concerns
 */
@Component
@Aspect
public class ValidationAspect {
    
    /**
     * POINTCUT: Matches methods that create users
     */
    @Pointcut("execution(* com.example.aoplab.service.UserService.createUser(..))")
    public void createUserMethods() {}
    
    /**
     * POINTCUT: Matches methods that update users
     */
    @Pointcut("execution(* com.example.aoplab.service.UserService.updateUser(..))")
    public void updateUserMethods() {}
    
    /**
     * ADVICE: Validate input parameters before creating a user
     */
    // TODO 14: Uncomment the @Before annotation to enable input validation
    // @Before("createUserMethods()")
    public void validateCreateUserInput(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        
        System.out.println("=== INPUT VALIDATION (CREATE USER) ===");
        
        if (args.length >= 3) {
            String username = (String) args[0];
            String fullName = (String) args[1];
            String email = (String) args[2];
            
            // Validate username
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            if (username.length() < 3) {
                throw new IllegalArgumentException("Username must be at least 3 characters long");
            }
            
            // Validate full name
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be null or empty");
            }
            
            // Validate email
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Email must contain @ symbol");
            }
            
            System.out.println("Validation passed for username: " + username);
            System.out.println("Validation passed for fullName: " + fullName);
            System.out.println("Validation passed for email: " + email);
        }
        
        System.out.println("=====================================");
    }
    
    /**
     * ADVICE: Validate input parameters before updating a user
     */
    // TODO 15: Uncomment the @Before annotation to enable update validation
    // @Before("updateUserMethods()")
    public void validateUpdateUserInput(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        
        System.out.println("=== INPUT VALIDATION (UPDATE USER) ===");
        
        if (args.length >= 3) {
            Long id = (Long) args[0];
            String fullName = (String) args[1];
            String email = (String) args[2];
            
            // Validate ID
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("User ID must be a positive number");
            }
            
            // Validate full name
            if (fullName == null || fullName.trim().isEmpty()) {
                throw new IllegalArgumentException("Full name cannot be null or empty");
            }
            
            // Validate email
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Email must contain @ symbol");
            }
            
            System.out.println("Validation passed for ID: " + id);
            System.out.println("Validation passed for fullName: " + fullName);
            System.out.println("Validation passed for email: " + email);
        }
        
        System.out.println("======================================");
    }
}
