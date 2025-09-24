package com.example.aoplab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * This aspect demonstrates performance monitoring using AOP
 * It shows how aspects can be used for cross-cutting concerns like performance measurement
 */
@Component
@Aspect
public class PerformanceAspect {
    
    /**
     * POINTCUT: Matches all service layer methods
     */
    @Pointcut("execution(* com.example.aoplab.service.*.*(..))")
    public void serviceLayerMethods() {}
    
    /**
     * ADVICE: Performance monitoring around all service methods
     * This demonstrates how AOP can be used for performance measurement
     */
    // TODO 11: Uncomment the @Around annotation to enable performance monitoring
    @Around("serviceLayerMethods()")
    public Object measurePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        
        try {
            // Execute the target method
            Object result = joinPoint.proceed();
            
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
            
            System.out.println("=== PERFORMANCE MONITORING ===");
            System.out.println("Method: " + joinPoint.getSignature().getName());
            System.out.println("Execution time: " + duration + " ms");
            System.out.println("===============================");
            
            return result;
        } catch (Exception e) {
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;
            
            System.out.println("=== PERFORMANCE MONITORING (ERROR) ===");
            System.out.println("Method: " + joinPoint.getSignature().getName());
            System.out.println("Execution time: " + duration + " ms (failed)");
            System.out.println("Error: " + e.getMessage());
            System.out.println("=====================================");
            
            throw e;
        }
    }
}
