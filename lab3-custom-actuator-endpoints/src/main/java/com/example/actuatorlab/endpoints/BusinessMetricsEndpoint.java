package com.example.actuatorlab.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;
import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "businessmetrics")
public class BusinessMetricsEndpoint {
    
    // TODO 11: Add counters to track business metrics
    // TODO 12: Uncomment the following code:
    /*
    private final AtomicLong totalOrders = new AtomicLong(0);
    private final AtomicLong totalRevenue = new AtomicLong(0);
    private final AtomicLong activeUsers = new AtomicLong(0);
    */
    
    // Placeholder counters - replace with actual implementation
    private final AtomicLong totalOrders = new AtomicLong(0);
    private final AtomicLong totalRevenue = new AtomicLong(0);
    private final AtomicLong activeUsers = new AtomicLong(0);
    
    // TODO 13: Add methods to update metrics
    // TODO 14: Uncomment the following methods:
    /*
    public void incrementOrders() {
        totalOrders.incrementAndGet();
    }
    
    public void addRevenue(long amount) {
        totalRevenue.addAndGet(amount);
    }
    
    public void setActiveUsers(long count) {
        activeUsers.set(count);
    }
    */
    
    @ReadOperation
    public Map<String, Object> getAllMetrics() {
        // TODO 15: Implement the getAllMetrics method
        // 1. Create a map with all metrics
        // 2. Calculate average order value
        // 3. Return the metrics map
        
        // TODO 16: Uncomment the following code:
        /*
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalOrders", totalOrders.get());
        metrics.put("totalRevenue", totalRevenue.get());
        metrics.put("activeUsers", activeUsers.get());
        metrics.put("averageOrderValue", 
            totalOrders.get() > 0 ? (double) totalRevenue.get() / totalOrders.get() : 0.0);
        return metrics;
        */
        
        // Placeholder return - replace with actual implementation
        Map<String, Object> placeholderMetrics = new HashMap<>();
        placeholderMetrics.put("status", "Business metrics not implemented yet");
        return placeholderMetrics;
    }
    
    @ReadOperation
    public Object getMetric(@Selector String metricName) {
        // TODO 17: Implement the getMetric method
        // 1. Use switch expression to handle different metric names
        // 2. Return appropriate metric value based on metricName
        // 3. Handle unknown metrics with error message
        
        // TODO 18: Uncomment the following code:
        /*
        return switch (metricName.toLowerCase()) {
            case "orders" -> totalOrders.get();
            case "revenue" -> totalRevenue.get();
            case "users" -> activeUsers.get();
            case "average" -> totalOrders.get() > 0 ? 
                (double) totalRevenue.get() / totalOrders.get() : 0.0;
            default -> Map.of("error", "Unknown metric: " + metricName);
        };
        */
        
        // Placeholder return - replace with actual implementation
        return Map.of("error", "Metric retrieval not implemented yet");
    }
} 