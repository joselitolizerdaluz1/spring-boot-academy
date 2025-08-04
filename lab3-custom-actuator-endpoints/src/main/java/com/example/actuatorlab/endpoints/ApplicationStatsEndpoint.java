package com.example.actuatorlab.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "appstats")
public class ApplicationStatsEndpoint {
    
    // TODO 1: Add a map to store application statistics
    // TODO 2: Uncomment the following code:
    /*
    private final Map<String, Object> stats = new HashMap<>();
    
    public ApplicationStatsEndpoint() {
        // Initialize with some default stats
        stats.put("startTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        stats.put("version", "1.0.0");
        stats.put("environment", "development");
    }
    */
    
    // Placeholder field - replace with actual implementation
    private final Map<String, Object> stats = new HashMap<>();
    
    @ReadOperation
    public Map<String, Object> getStats() {
        // TODO 3: Implement the getStats method
        // 1. Add current timestamp to stats
        // 2. Add uptime to stats
        // 3. Return the stats map
        
        // TODO 4: Uncomment the following code to implement the method:
        /*
        // Add current timestamp
        stats.put("lastUpdated", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        stats.put("uptime", System.currentTimeMillis());
        
        return stats;
        */
        
        // Placeholder return - replace with actual implementation
        Map<String, Object> placeholderStats = new HashMap<>();
        placeholderStats.put("status", "Application stats not implemented yet");
        return placeholderStats;
    }
} 