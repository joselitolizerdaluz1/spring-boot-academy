package com.example.aoplab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * This aspect demonstrates security-related cross-cutting concerns
 * It shows how AOP can be used for authentication and authorization
 */
@Component
@Aspect
public class SecurityAspect {
    
    /**
     * POINTCUT: Matches all controller methods
     */
    @Pointcut("execution(* com.example.aoplab.controller.*.*(..))")
    public void controllerMethods() {}
    
    /**
     * POINTCUT: Matches methods that modify data (POST, PUT, DELETE)
     */
    @Pointcut("execution(* com.example.aoplab.controller.*.create*(..)) || " +
              "execution(* com.example.aoplab.controller.*.update*(..)) || " +
              "execution(* com.example.aoplab.controller.*.delete*(..))")
    public void modificationMethods() {}
    
    /**
     * ADVICE: Simulate authentication check before controller methods
     */
    // TODO 12: Uncomment the @Before annotation to enable authentication
    // @Before("controllerMethods()")
    public void checkAuthentication(JoinPoint joinPoint) {
        System.out.println("=== AUTHENTICATION CHECK ===");
        System.out.println("Checking authentication for: " + joinPoint.getSignature().getName());
        System.out.println("User authenticated: " + simulateAuthentication());
        System.out.println("=============================");
    }
    
    /**
     * ADVICE: Simulate authorization check for modification methods
     */
    // TODO 13: Uncomment the @Around annotation to enable authorization
    // @Around("modificationMethods()")
    public Object checkAuthorization(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("=== AUTHORIZATION CHECK ===");
        System.out.println("Checking authorization for: " + joinPoint.getSignature().getName());
        
        if (simulateAuthorization()) {
            System.out.println("User authorized - proceeding with operation");
            return joinPoint.proceed();
        } else {
            System.out.println("User not authorized - denying access");
            throw new SecurityException("Access denied: Insufficient permissions");
        }
    }
    
    /**
     * Simulate authentication logic
     */
    private boolean simulateAuthentication() {
        // In a real application, this would check JWT tokens, session data, etc.
        return true; // Simulate successful authentication
    }
    
    /**
     * Simulate authorization logic
     */
    private boolean simulateAuthorization() {
        // In a real application, this would check user roles and permissions
        return true; // Simulate successful authorization
    }
}
