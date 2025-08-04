package com.example.actuatorlab.health;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class BusinessLogicHealthIndicator implements HealthIndicator {
    
    // TODO 9: Add counters to track requests and errors
    // TODO 10: Uncomment the following code:
    /*
    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    */
    
    // Placeholder counters - replace with actual implementation
    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    
    // TODO 11: Add methods to increment counters
    // TODO 12: Uncomment the following methods:
    /*
    public void incrementRequestCount() {
        requestCount.incrementAndGet();
    }
    
    public void incrementErrorCount() {
        errorCount.incrementAndGet();
    }
    */
    
    @Override
    public Health health() {
        // TODO 13: Implement business logic health check
        // 1. Get total requests and errors
        // 2. Calculate error rate
        // 3. Return appropriate health status based on error rate:
        //    - UP if error rate <= 5%
        //    - DEGRADED if error rate > 5% and <= 10%
        //    - DOWN if error rate > 10%
        // 4. Include details like error rate, total requests, total errors
        
        // TODO 14: Uncomment the following code to implement the health check:
        /*
        long totalRequests = requestCount.get();
        long totalErrors = errorCount.get();
        
        if (totalRequests == 0) {
            return Health.unknown()
                    .withDetail("status", "No requests processed yet")
                    .build();
        }
        
        double errorRate = (double) totalErrors / totalRequests;
        
        if (errorRate > 0.1) { // More than 10% error rate
            return Health.down()
                    .withDetail("error-rate", String.format("%.2f%%", errorRate * 100))
                    .withDetail("total-requests", totalRequests)
                    .withDetail("total-errors", totalErrors)
                    .build();
        } else if (errorRate > 0.05) { // More than 5% error rate
            return Health.status("DEGRADED")
                    .withDetail("error-rate", String.format("%.2f%%", errorRate * 100))
                    .withDetail("total-requests", totalRequests)
                    .withDetail("total-errors", totalErrors)
                    .build();
        } else {
            return Health.up()
                    .withDetail("error-rate", String.format("%.2f%%", errorRate * 100))
                    .withDetail("total-requests", totalRequests)
                    .withDetail("total-errors", totalErrors)
                    .build();
        }
        */
        
        // Placeholder return - replace with actual implementation
        return Health.unknown()
                .withDetail("status", "Business logic health check not implemented")
                .build();
    }
} 