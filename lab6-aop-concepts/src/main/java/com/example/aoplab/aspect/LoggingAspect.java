package com.example.aoplab.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * This aspect demonstrates all five key AOP concepts:
 * 1. Aspect: This class itself is an aspect (cross-cutting concern)
 * 2. Advice: The methods annotated with @Before, @After, etc. are advice
 * 3. Join Point: The specific points in the execution where advice is applied
 * 4. Pointcut: The expressions that define where advice should be applied
 * 5. Weaving: The process of applying aspects to target objects (done by Spring)
 */
@Component
@Aspect
public class LoggingAspect {
    
    /**
     * POINTCUT: Defines a reusable pointcut expression
     * This pointcut matches all public methods in the UserService class
     */
    @Pointcut("execution(public * com.example.aoplab.service.UserService.*(..))")
    public void userServiceMethods() {}
    
    /**
     * POINTCUT: Matches methods that take a Long parameter (like findUserById, updateUser, deleteUser)
     */
    @Pointcut("execution(* com.example.aoplab.service.UserService.*(Long, ..))")
    public void userServiceMethodsWithLongParam() {}
    
    /**
     * POINTCUT: Matches methods that return a User object
     */
    @Pointcut("execution(com.example.aoplab.service.UserService.User com.example.aoplab.service.UserService.*(..))")
    public void userServiceMethodsReturningUser() {}
    
    /**
     * ADVICE TYPE: @Before
     * JOIN POINT: Before method execution
     * This advice runs before the target method executes
     */
    // TODO 2: Uncomment the @Before annotation to enable before advice for createUser method
    // @Before("execution(* com.example.aoplab.service.UserService.createUser(..))")
    public void logBeforeCreateUser(JoinPoint joinPoint) {
        System.out.println("=== BEFORE ADVICE ===");
        System.out.println("About to create user with arguments: " + Arrays.toString(joinPoint.getArgs()));
        System.out.println("Method signature: " + joinPoint.getSignature());
        System.out.println("Target class: " + joinPoint.getTarget().getClass().getSimpleName());
        System.out.println("====================");
    }
    
    /**
     * ADVICE TYPE: @AfterReturning
     * JOIN POINT: After successful method execution
     * This advice runs after the target method completes successfully
     */
    // TODO 3: Uncomment the @AfterReturning annotation to enable after returning advice
    // @AfterReturning(pointcut = "execution(* com.example.aoplab.service.UserService.findUserById(..))", returning = "result")
    public void logAfterReturningFindUser(JoinPoint joinPoint, Object result) {
        System.out.println("=== AFTER RETURNING ADVICE ===");
        System.out.println("Method: " + joinPoint.getSignature().getName());
        System.out.println("Arguments: " + Arrays.toString(joinPoint.getArgs()));
        System.out.println("Return value: " + result);
        System.out.println("===============================");
    }
    
    /**
     * ADVICE TYPE: @Around
     * JOIN POINT: Before and after method execution (can control method execution)
     * This advice can execute code before, after, or instead of the target method
     */
    // TODO 4: Uncomment the @Around annotation to enable around advice
    // @Around("execution(* com.example.aoplab.service.UserService.updateUser(..))")
    public Object logAroundUpdateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("=== AROUND ADVICE - BEFORE ===");
        System.out.println("About to execute: " + joinPoint.getSignature().getName());
        System.out.println("Arguments: " + Arrays.toString(joinPoint.getArgs()));
        
        long startTime = System.currentTimeMillis();
        
        // Execute the target method
        Object result = joinPoint.proceed();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("=== AROUND ADVICE - AFTER ===");
        System.out.println("Method completed in: " + (endTime - startTime) + " ms");
        System.out.println("Return value: " + result);
        System.out.println("=============================");
        
        return result;
    }
    
    /**
     * ADVICE TYPE: @AfterThrowing
     * JOIN POINT: After method throws an exception
     * This advice runs when the target method throws an exception
     */
    // TODO 5: Uncomment the @AfterThrowing annotation to enable after throwing advice
    // @AfterThrowing(pointcut = "execution(* com.example.aoplab.service.UserService.deleteUser(..))", throwing = "exception")
    public void logAfterThrowingDeleteUser(JoinPoint joinPoint, Exception exception) {
        System.out.println("=== AFTER THROWING ADVICE ===");
        System.out.println("Method: " + joinPoint.getSignature().getName());
        System.out.println("Arguments: " + Arrays.toString(joinPoint.getArgs()));
        System.out.println("Exception: " + exception.getMessage());
        System.out.println("Exception type: " + exception.getClass().getSimpleName());
        System.out.println("=============================");
    }
    
    /**
     * ADVICE TYPE: @After
     * JOIN POINT: After method execution (regardless of success or failure)
     * This advice runs after the target method completes, whether it succeeds or throws an exception
     */
    // TODO 6: Uncomment the @After annotation to enable after advice
    // @After("execution(* com.example.aoplab.service.UserService.getAllUsers(..))")
    public void logAfterGetAllUsers(JoinPoint joinPoint) {
        System.out.println("=== AFTER ADVICE ===");
        System.out.println("Method: " + joinPoint.getSignature().getName() + " has completed");
        System.out.println("Arguments: " + Arrays.toString(joinPoint.getArgs()));
        System.out.println("===================");
    }
    
    /**
     * ADVICE: Demonstrates using reusable pointcuts
     * This advice uses the userServiceMethods() pointcut we defined earlier
     */
    // TODO 7: Uncomment the @Before annotation to enable pointcut reuse
    // @Before("userServiceMethods()")
    public void logAllUserServiceMethods(JoinPoint joinPoint) {
        System.out.println("--- POINTCUT REUSE: Executing " + joinPoint.getSignature().getName() + " ---");
    }
    
    /**
     * ADVICE: Demonstrates conditional advice based on method parameters
     * This advice only runs for methods that take a Long parameter
     */
    // TODO 8: Uncomment the @Before annotation to enable parameter-based advice
    // @Before("userServiceMethodsWithLongParam()")
    public void logMethodsWithLongParam(JoinPoint joinPoint) {
        System.out.println("--- METHOD WITH LONG PARAM: " + joinPoint.getSignature().getName() + " ---");
        System.out.println("First parameter (Long): " + joinPoint.getArgs()[0]);
    }
    
    /**
     * ADVICE: Demonstrates advice for methods returning specific types
     * This advice only runs for methods that return a User object
     */
    // TODO 9: Uncomment the @AfterReturning annotation to enable return type-based advice
    // @AfterReturning(pointcut = "userServiceMethodsReturningUser()", returning = "result")
    public void logUserReturningMethods(JoinPoint joinPoint, Object result) {
        System.out.println("--- USER RETURNING METHOD: " + joinPoint.getSignature().getName() + " ---");
        System.out.println("Returned User: " + result);
    }
    
    /**
     * ADVICE: Demonstrates exception handling in aspects
     * This advice catches all exceptions from UserService methods
     */
    // TODO 10: Uncomment the @AfterThrowing annotation to enable global exception logging
    // @AfterThrowing(pointcut = "userServiceMethods()", throwing = "exception")
    public void logAllExceptions(JoinPoint joinPoint, Exception exception) {
        System.out.println("=== GLOBAL EXCEPTION LOGGING ===");
        System.out.println("Exception in method: " + joinPoint.getSignature().getName());
        System.out.println("Exception message: " + exception.getMessage());
        System.out.println("Stack trace:");
        exception.printStackTrace();
        System.out.println("=================================");
    }
}
