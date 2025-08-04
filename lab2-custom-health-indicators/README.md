# Lab 2: Creating Custom Health Indicators

## Difficulty Level
**Intermediate**

## Learning Objectives
- Understand how Spring Boot Health Indicators work
- Create custom health indicators for application-specific checks
- Implement health checks for external dependencies
- Configure health indicator behavior and thresholds
- Learn to aggregate multiple health indicators

## Scenario
Your Spring Boot application depends on several external services (database, cache, external API). You need to create custom health indicators to monitor these dependencies and provide detailed health information. This will help your operations team quickly identify which specific component is causing issues.

## Initial Project Setup Guidance

### Dependencies Required
Add these to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'com.h2database:h2'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Step-by-step Instructions

### 1. Project Setup
1. Start with the project from Lab 1 or create a new Spring Boot project
2. Add the dependencies listed above
3. Create the necessary package structure for health indicators

### 2. Create a Database Health Indicator
Create `src/main/java/com/example/actuatorlab/health/DatabaseHealthIndicator.java`:

```java
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
        try {
            // Simple query to check database connectivity
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Health.up()
                    .withDetail("database", "H2 Database")
                    .withDetail("status", "Connected")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "H2 Database")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
```

### 3. Create an External API Health Indicator
Create `src/main/java/com/example/actuatorlab/health/ExternalApiHealthIndicator.java`:

```java
package com.example.actuatorlab.health;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ExternalApiHealthIndicator implements HealthIndicator {
    
    private final WebClient webClient;
    
    public ExternalApiHealthIndicator(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://httpbin.org")
                .build();
    }
    
    @Override
    public Health health() {
        try {
            // Check external API with timeout
            String response = webClient.get()
                    .uri("/status/200")
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            
            return Health.up()
                    .withDetail("external-api", "httpbin.org")
                    .withDetail("response", "OK")
                    .withDetail("response-time", "Under 5 seconds")
                    .build();
                    
        } catch (Exception e) {
            return Health.down()
                    .withDetail("external-api", "httpbin.org")
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
```

### 4. Create a Custom Business Logic Health Indicator
Create `src/main/java/com/example/actuatorlab/health/BusinessLogicHealthIndicator.java`:

```java
package com.example.actuatorlab.health;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class BusinessLogicHealthIndicator implements HealthIndicator {
    
    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong errorCount = new AtomicLong(0);
    
    public void incrementRequestCount() {
        requestCount.incrementAndGet();
    }
    
    public void incrementErrorCount() {
        errorCount.incrementAndGet();
    }
    
    @Override
    public Health health() {
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
    }
}
```

### 5. Update the Controller to Use Health Indicators
Update `src/main/java/com/example/actuatorlab/HelloController.java`:

```java
package com.example.actuatorlab;

import com.example.actuatorlab.health.BusinessLogicHealthIndicator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    private final BusinessLogicHealthIndicator businessLogicHealthIndicator;
    
    public HelloController(BusinessLogicHealthIndicator businessLogicHealthIndicator) {
        this.businessLogicHealthIndicator = businessLogicHealthIndicator;
    }
    
    @GetMapping("/hello")
    public String hello() {
        businessLogicHealthIndicator.incrementRequestCount();
        return "Hello, Spring Boot Actuator with Custom Health Indicators!";
    }
    
    @GetMapping("/error-simulation")
    public String simulateError() {
        businessLogicHealthIndicator.incrementRequestCount();
        businessLogicHealthIndicator.incrementErrorCount();
        return "Error simulated - check health endpoint";
    }
}
```

### 6. Configure Application Properties
Update `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true

# Actuator configuration
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

# Custom health indicator configuration
management.health.defaults.enabled=true
management.health.diskspace.enabled=true

# Application info
info.app.name=Custom Health Indicators Lab
info.app.description=Learning to create custom health indicators
info.app.version=2.0.0
```

### 7. Run and Test
```bash
./gradlew bootRun
```

### 8. Test Health Endpoints
```bash
# Check overall health
curl http://localhost:8080/actuator/health

# Check specific health indicators
curl http://localhost:8080/actuator/health/db
curl http://localhost:8080/actuator/health/externalApi
curl http://localhost:8080/actuator/health/businessLogic

# Simulate some requests
curl http://localhost:8080/hello
curl http://localhost:8080/error-simulation

# Check business logic health again
curl http://localhost:8080/actuator/health/businessLogic
```

## Verification Steps

### 1. Health Indicator Verification
- ✅ Confirm that `/actuator/health` shows all custom health indicators
- ✅ Verify that database health indicator shows "UP" status
- ✅ Check that external API health indicator responds correctly
- ✅ Test business logic health indicator with different scenarios

### 2. Expected Output Examples

**Overall health response:**
```json
{
  "status": "UP",
  "components": {
    "businessLogic": {
      "status": "UP",
      "details": {
        "error-rate": "0.00%",
        "total-requests": 5,
        "total-errors": 0
      }
    },
    "db": {
      "status": "UP",
      "details": {
        "database": "H2 Database",
        "status": "Connected"
      }
    },
    "externalApi": {
      "status": "UP",
      "details": {
        "external-api": "httpbin.org",
        "response": "OK",
        "response-time": "Under 5 seconds"
      }
    }
  }
}
```

### 3. Error Simulation Test
1. Call `/error-simulation` endpoint multiple times
2. Check `/actuator/health/businessLogic` to see error rate increase
3. Verify status changes from "UP" to "DEGRADED" or "DOWN"

## Reflection Questions

1. **What are the benefits of creating custom health indicators?**
   - Application-specific monitoring
   - Early detection of issues
   - Detailed health information for operations teams
   - Integration with monitoring systems

2. **How would you handle health indicators that depend on external services?**
   - Implement timeouts to prevent hanging
   - Use circuit breaker patterns
   - Cache health results temporarily
   - Provide fallback mechanisms

3. **What considerations should you have when designing health indicators for production?**
   - Performance impact of health checks
   - Security of health information
   - Appropriate thresholds and alerting
   - Integration with monitoring tools

4. **How could you extend the business logic health indicator for more complex scenarios?**
   - Add more metrics (response times, throughput)
   - Implement sliding windows for error rates
   - Add custom thresholds based on business requirements
   - Integrate with logging and metrics systems

## Key Concepts Covered

- **HealthIndicator Interface**: Core interface for custom health checks
- **Health.Builder**: Fluent API for building health responses
- **Health Statuses**: UP, DOWN, UNKNOWN, and custom statuses
- **Health Details**: Adding contextual information to health responses
- **Component Health**: Organizing health indicators by component
- **Timeout Handling**: Managing external service health checks
- **Business Metrics**: Converting business logic into health indicators
- **Health Aggregation**: How Spring Boot combines multiple health indicators

## Resources

- [Spring Boot Health Indicators Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.health)
- [Baeldung: Custom Health Indicators](https://www.baeldung.com/spring-boot-health-indicators)
- [Spring Boot Health Endpoint Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health)
- [Spring Boot WebClient Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-client)

## Next Steps
After completing this lab, you should be comfortable with:
- Creating custom health indicators
- Implementing health checks for external dependencies
- Configuring health indicator behavior
- Understanding health status aggregation

This prepares you for more advanced actuator features like custom endpoints and metrics in subsequent labs. 