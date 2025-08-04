package com.example.actuatorlab.health;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Health health() {
        // TODO 1: Implement database health check
        // 1. Use jdbcTemplate to execute a simple query like "SELECT 1"
        // 2. Return Health.up() with database details if successful
        // 3. Return Health.down() with error details if exception occurs
        
        try {
            // TODO 2: Uncomment the following code to implement the health check:
            /*
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up()
                    .withDetail("database", "H2 Database")
                    .withDetail("status", "Connected")
                    .build();
            */
            
            // Placeholder return - replace with actual implementation
            return Health.unknown()
                    .withDetail("database", "H2 Database")
                    .withDetail("status", "Not implemented yet")
                    .build();
                    
        } catch (Exception e) {
            // TODO 3: Uncomment the following code to handle exceptions:
            /*
            return Health.down()
                    .withDetail("database", "H2 Database")
                    .withDetail("error", e.getMessage())
                    .build();
            */
            
            // Placeholder return - replace with actual implementation
            return Health.down()
                    .withDetail("database", "H2 Database")
                    .withDetail("error", "Health check not implemented")
                    .build();
        }
    }
} 