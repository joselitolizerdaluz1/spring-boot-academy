package com.example.actuatorlab;

import com.example.actuatorlab.endpoints.BusinessMetricsEndpoint;
import com.example.actuatorlab.endpoints.ApplicationManagementEndpoint;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HelloController {
    
    // TODO 19: Inject the required endpoints
    // TODO 20: Uncomment the following code:
    /*
    private final BusinessMetricsEndpoint businessMetricsEndpoint;
    private final ApplicationManagementEndpoint appManagementEndpoint;
    
    public HelloController(BusinessMetricsEndpoint businessMetricsEndpoint,
                          ApplicationManagementEndpoint appManagementEndpoint) {
        this.businessMetricsEndpoint = businessMetricsEndpoint;
        this.appManagementEndpoint = appManagementEndpoint;
    }
    */
    
    // Placeholder fields - replace with actual implementation
    private final BusinessMetricsEndpoint businessMetricsEndpoint = null;
    private final ApplicationManagementEndpoint appManagementEndpoint = null;
    
    @GetMapping("/hello")
    public String hello() {
        // TODO 21: Simulate business activity by updating metrics
        // TODO 22: Uncomment the following code:
        /*
        // Simulate business activity
        businessMetricsEndpoint.incrementOrders();
        businessMetricsEndpoint.addRevenue(100);
        
        return "Hello, Spring Boot Actuator with Custom Endpoints!";
        */
        
        // Placeholder return - replace with actual implementation
        return "Hello, Spring Boot Actuator with Custom Endpoints! (Not implemented yet)";
    }
    
    @PostMapping("/order")
    public String createOrder(@RequestBody Map<String, Object> order) {
        // TODO 23: Process order and update metrics
        // TODO 24: Uncomment the following code:
        /*
        Long amount = Long.valueOf(order.get("amount").toString());
        businessMetricsEndpoint.incrementOrders();
        businessMetricsEndpoint.addRevenue(amount);
        
        return "Order created with amount: " + amount;
        */
        
        // Placeholder return - replace with actual implementation
        return "Order creation not implemented yet";
    }
    
    @GetMapping("/status")
    public Map<String, Object> getApplicationStatus() {
        // TODO 25: Return application management status
        // TODO 26: Uncomment the following code:
        /*
        return appManagementEndpoint.getStatus();
        */
        
        // Placeholder return - replace with actual implementation
        return Map.of("status", "Application status not implemented yet");
    }
} 