# Lab 3: Creating Custom Actuator Endpoints

## Difficulty Level
**Intermediate**

## Learning Objectives
- Understand the structure of Spring Boot Actuator endpoints
- Create custom read-only endpoints for application information
- Implement writable endpoints for application management
- Configure endpoint security and exposure
- Learn to organize endpoints by domain and functionality
- Understand endpoint response formats and HTTP methods

## Scenario
Your Spring Boot application needs custom management endpoints to provide application-specific information and allow runtime configuration changes. You want to create endpoints for viewing application statistics, managing feature flags, and performing application-specific operations that aren't covered by standard actuator endpoints.

## Initial Project Setup Guidance

### Dependencies Required
Add these to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'com.h2database:h2'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Step-by-step Instructions

### 1. Project Setup
1. Start with the project from Lab 2 or create a new Spring Boot project
2. Add the dependencies listed above
3. Create the necessary package structure for custom endpoints

### 2. Create a Read-Only Application Statistics Endpoint
Create `src/main/java/com/example/actuatorlab/endpoints/ApplicationStatsEndpoint.java`:

```java
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
    
    private final Map<String, Object> stats = new HashMap<>();
    
    public ApplicationStatsEndpoint() {
        // Initialize with some default stats
        stats.put("startTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        stats.put("version", "1.0.0");
        stats.put("environment", "development");
    }
    
    @ReadOperation
    public Map<String, Object> getStats() {
        // Add current timestamp
        stats.put("lastUpdated", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        stats.put("uptime", System.currentTimeMillis());
        
        return stats;
    }
}
```

### 3. Create a Writable Feature Flag Management Endpoint
Create `src/main/java/com/example/actuatorlab/endpoints/FeatureFlagsEndpoint.java`:

```java
package com.example.actuatorlab.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "featureflags")
public class FeatureFlagsEndpoint {
    
    private final Map<String, Boolean> featureFlags = new HashMap<>();
    
    public FeatureFlagsEndpoint() {
        // Initialize with some default feature flags
        featureFlags.put("new-ui", false);
        featureFlags.put("beta-features", true);
        featureFlags.put("debug-mode", false);
    }
    
    @ReadOperation
    public Map<String, Boolean> getFeatureFlags() {
        return new HashMap<>(featureFlags);
    }
    
    @WriteOperation
    public void updateFeatureFlag(String name, Boolean enabled) {
        if (name != null && enabled != null) {
            featureFlags.put(name, enabled);
        }
    }
}
```

### 4. Create a Custom Business Metrics Endpoint
Create `src/main/java/com/example/actuatorlab/endpoints/BusinessMetricsEndpoint.java`:

```java
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
    
    private final AtomicLong totalOrders = new AtomicLong(0);
    private final AtomicLong totalRevenue = new AtomicLong(0);
    private final AtomicLong activeUsers = new AtomicLong(0);
    
    public void incrementOrders() {
        totalOrders.incrementAndGet();
    }
    
    public void addRevenue(long amount) {
        totalRevenue.addAndGet(amount);
    }
    
    public void setActiveUsers(long count) {
        activeUsers.set(count);
    }
    
    @ReadOperation
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalOrders", totalOrders.get());
        metrics.put("totalRevenue", totalRevenue.get());
        metrics.put("activeUsers", activeUsers.get());
        metrics.put("averageOrderValue", 
            totalOrders.get() > 0 ? (double) totalRevenue.get() / totalOrders.get() : 0.0);
        return metrics;
    }
    
    @ReadOperation
    public Object getMetric(@Selector String metricName) {
        return switch (metricName.toLowerCase()) {
            case "orders" -> totalOrders.get();
            case "revenue" -> totalRevenue.get();
            case "users" -> activeUsers.get();
            case "average" -> totalOrders.get() > 0 ? 
                (double) totalRevenue.get() / totalOrders.get() : 0.0;
            default -> Map.of("error", "Unknown metric: " + metricName);
        };
    }
}
```

### 5. Create an Application Management Endpoint
Create `src/main/java/com/example/actuatorlab/endpoints/ApplicationManagementEndpoint.java`:

```java
package com.example.actuatorlab.endpoints;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Endpoint(id = "appmanagement")
public class ApplicationManagementEndpoint {
    
    private final Map<String, String> configuration = new ConcurrentHashMap<>();
    private boolean maintenanceMode = false;
    
    public ApplicationManagementEndpoint() {
        configuration.put("cache.ttl", "3600");
        configuration.put("max.connections", "100");
        configuration.put("timeout", "30");
    }
    
    @ReadOperation
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("maintenanceMode", maintenanceMode);
        status.put("configuration", new HashMap<>(configuration));
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }
    
    @WriteOperation
    public void setMaintenanceMode(boolean enabled) {
        this.maintenanceMode = enabled;
    }
    
    @WriteOperation
    public void updateConfiguration(String key, String value) {
        if (key != null && value != null) {
            configuration.put(key, value);
        }
    }
}
```

### 6. Update the Controller to Use Custom Endpoints
Update `src/main/java/com/example/actuatorlab/HelloController.java`:

```java
package com.example.actuatorlab;

import com.example.actuatorlab.endpoints.BusinessMetricsEndpoint;
import com.example.actuatorlab.endpoints.ApplicationManagementEndpoint;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class HelloController {
    
    private final BusinessMetricsEndpoint businessMetricsEndpoint;
    private final ApplicationManagementEndpoint appManagementEndpoint;
    
    public HelloController(BusinessMetricsEndpoint businessMetricsEndpoint,
                          ApplicationManagementEndpoint appManagementEndpoint) {
        this.businessMetricsEndpoint = businessMetricsEndpoint;
        this.appManagementEndpoint = appManagementEndpoint;
    }
    
    @GetMapping("/hello")
    public String hello() {
        // Simulate business activity
        businessMetricsEndpoint.incrementOrders();
        businessMetricsEndpoint.addRevenue(100);
        
        return "Hello, Spring Boot Actuator with Custom Endpoints!";
    }
    
    @PostMapping("/order")
    public String createOrder(@RequestBody Map<String, Object> order) {
        Long amount = Long.valueOf(order.get("amount").toString());
        businessMetricsEndpoint.incrementOrders();
        businessMetricsEndpoint.addRevenue(amount);
        
        return "Order created with amount: " + amount;
    }
    
    @GetMapping("/status")
    public Map<String, Object> getApplicationStatus() {
        return appManagementEndpoint.getStatus();
    }
}
```

### 7. Configure Application Properties
Update `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Actuator configuration
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

# Custom endpoint configuration
management.endpoint.appstats.enabled=true
management.endpoint.featureflags.enabled=true
management.endpoint.businessmetrics.enabled=true
management.endpoint.appmanagement.enabled=true

# Application info
info.app.name=Custom Endpoints Lab
info.app.description=Learning to create custom actuator endpoints
info.app.version=3.0.0
```

### 8. Run and Test
```bash
./gradlew bootRun
```

### 9. Test Custom Endpoints
```bash
# Read application statistics
curl http://localhost:8080/actuator/appstats

# Read feature flags
curl http://localhost:8080/actuator/featureflags

# Update a feature flag
curl -X POST http://localhost:8080/actuator/featureflags \
  -H "Content-Type: application/json" \
  -d '{"name":"new-ui","enabled":true}'

# Read business metrics
curl http://localhost:8080/actuator/businessmetrics
curl http://localhost:8080/actuator/businessmetrics/orders
curl http://localhost:8080/actuator/businessmetrics/revenue

# Get application management status
curl http://localhost:8080/actuator/appmanagement

# Enable maintenance mode
curl -X POST http://localhost:8080/actuator/appmanagement \
  -H "Content-Type: application/json" \
  -d '{"enabled":true}'

# Update configuration
curl -X POST http://localhost:8080/actuator/appmanagement \
  -H "Content-Type: application/json" \
  -d '{"key":"cache.ttl","value":"7200"}'

# Simulate business activity
curl http://localhost:8080/hello
curl -X POST http://localhost:8080/order \
  -H "Content-Type: application/json" \
  -d '{"amount":250}'
```

## Verification Steps

### 1. Endpoint Verification
- ✅ Confirm that all custom endpoints are accessible via `/actuator`
- ✅ Verify that read operations return expected data
- ✅ Test write operations and verify changes are persisted
- ✅ Check that selector-based endpoints work correctly

### 2. Expected Output Examples

**/actuator/appstats response:**
```json
{
  "startTime": "2024-01-15T10:30:00",
  "version": "1.0.0",
  "environment": "development",
  "lastUpdated": "2024-01-15T11:45:30",
  "uptime": 1705316730000
}
```

**/actuator/featureflags response:**
```json
{
  "new-ui": false,
  "beta-features": true,
  "debug-mode": false
}
```

**/actuator/businessmetrics response:**
```json
{
  "totalOrders": 5,
  "totalRevenue": 750,
  "activeUsers": 0,
  "averageOrderValue": 150.0
}
```

### 3. Write Operation Testing
1. Update feature flags and verify changes
2. Enable maintenance mode and check status
3. Update configuration values and confirm persistence
4. Simulate business activity and verify metrics update

## Reflection Questions

1. **What are the advantages of creating custom actuator endpoints?**
   - Application-specific management capabilities
   - Runtime configuration without restart
   - Business metrics and statistics
   - Integration with monitoring and management tools

2. **How would you secure custom endpoints in production?**
   - Use Spring Security with role-based access
   - Implement authentication and authorization
   - Consider using different exposure levels for different environments
   - Add audit logging for sensitive operations

3. **What considerations should you have when designing writable endpoints?**
   - Input validation and sanitization
   - Idempotency for safe retries
   - Rollback mechanisms for configuration changes
   - Impact on application performance and stability

4. **How could you extend these endpoints for more complex scenarios?**
   - Add pagination for large datasets
   - Implement filtering and sorting
   - Add bulk operations for efficiency
   - Integrate with external systems and databases

## Key Concepts Covered

- **@Endpoint Annotation**: Core annotation for custom actuator endpoints
- **@ReadOperation**: Annotation for read-only endpoint operations
- **@WriteOperation**: Annotation for writable endpoint operations
- **@Selector**: Annotation for path-based parameter selection
- **Endpoint Exposure**: Configuring which endpoints are available
- **HTTP Methods**: Mapping operations to appropriate HTTP methods
- **Response Formats**: Structuring endpoint responses
- **Endpoint Security**: Considerations for securing custom endpoints
- **Business Logic Integration**: Connecting endpoints to application functionality

## Resources

- [Spring Boot Actuator Custom Endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.implementing)
- [Baeldung: Custom Actuator Endpoints](https://www.baeldung.com/spring-boot-actuator-custom-endpoints)
- [Spring Boot Endpoint Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)
- [Spring Boot Actuator Security](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.security)

## Next Steps
After completing this lab, you should be comfortable with:
- Creating custom read-only endpoints
- Implementing writable endpoints for runtime management
- Using selectors for parameterized endpoints
- Configuring endpoint exposure and security

This prepares you for advanced actuator features like metrics collection and custom health indicators in subsequent labs. 