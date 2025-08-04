package com.example.actuatorlab;

import com.example.actuatorlab.health.BusinessLogicHealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    // TODO 15: Inject the BusinessLogicHealthIndicator
    // TODO 16: Uncomment the following code:
    /*
    private final BusinessLogicHealthIndicator businessLogicHealthIndicator;
    
    public HelloController(BusinessLogicHealthIndicator businessLogicHealthIndicator) {
        this.businessLogicHealthIndicator = businessLogicHealthIndicator;
    }
    */
    
    // Placeholder field - replace with actual implementation
    private final BusinessLogicHealthIndicator businessLogicHealthIndicator = null;
    
    @GetMapping("/hello")
    public String hello() {
        // TODO 17: Increment request count in the health indicator
        // TODO 18: Uncomment the following code:
        /*
        businessLogicHealthIndicator.incrementRequestCount();
        return "Hello, Spring Boot Actuator with Custom Health Indicators!";
        */
        
        // Placeholder return - replace with actual implementation
        return "Hello, Spring Boot Actuator with Custom Health Indicators! (Not implemented yet)";
    }
    
    @GetMapping("/error-simulation")
    public String simulateError() {
        // TODO 19: Increment both request count and error count
        // TODO 20: Uncomment the following code:
        /*
        businessLogicHealthIndicator.incrementRequestCount();
        businessLogicHealthIndicator.incrementErrorCount();
        return "Error simulated - check health endpoint";
        */
        
        // Placeholder return - replace with actual implementation
        return "Error simulation not implemented yet";
    }
} 