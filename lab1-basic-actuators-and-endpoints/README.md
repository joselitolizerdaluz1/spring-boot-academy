# Lab 1: Enabling and Exploring Basic Actuator Endpoints

## Difficulty Level
**Easy**

## Learning Objectives
- Understand how to enable Spring Boot Actuator in a project
- Learn to access and interpret the default `/health` and `/info` endpoints
- Identify the basic information provided by Actuator out-of-the-box
- Configure basic Actuator properties

## Scenario
You've just created a new Spring Boot microservice and want to add basic operational monitoring. Your first step is to enable Spring Boot Actuator and see what information it provides by default. This will help you understand the health status of your application and provide basic operational insights.

## Initial Project Setup Guidance

### Option 1: Using start.spring.io
1. Go to https://start.spring.io/
2. Select the following dependencies:
   - **Spring Web**
   - **Spring Boot Actuator**
3. Generate and download the project

### Option 2: Manual Setup
Add these dependencies to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Step-by-step Instructions

### 1. Project Setup
1. Create a new Spring Boot project with the dependencies listed above
2. Ensure your `build.gradle` contains the required dependencies
3. Create a simple REST controller to test the application

### 2. Create a Basic Controller
Create `src/main/java/com/example/actuatorlab/HelloController.java`:

```java
package com.example.actuatorlab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot Actuator!";
    }
}
```

### 3. Configure Actuator (Optional)
Add to `src/main/resources/application.properties`:

```properties
# Expose all actuator endpoints
management.endpoints.web.exposure.include=*

# Show full health details
management.endpoint.health.show-details=always

# Custom application info
info.app.name=Spring Boot Actuator Lab
info.app.description=Learning Spring Boot Actuator
info.app.version=1.0.0
```

### 4. Run the Application
```bash
./gradlew bootRun
```

### 5. Access Default Endpoints
Use your browser or curl to access:

```bash
# List all available actuator endpoints
curl http://localhost:8080/actuator

# Check application health
curl http://localhost:8080/actuator/health

# Get application info
curl http://localhost:8080/actuator/info

# Get application metrics
curl http://localhost:8080/actuator/metrics

# Get specific metric (e.g., JVM memory)
curl http://localhost:8080/actuator/metrics/jvm.memory.used
```

### 6. Explore Additional Endpoints
Try these endpoints to see what information is available:

```bash
# Environment information
curl http://localhost:8080/actuator/env

# Configuration properties
curl http://localhost:8080/actuator/configprops

# Application mappings
curl http://localhost:8080/actuator/mappings

# Thread dump
curl http://localhost:8080/actuator/threaddump
```

## Verification Steps

### 1. Basic Endpoint Verification
- ✅ Confirm that `http://localhost:8080/actuator` returns a JSON object with available endpoints
- ✅ Verify that `http://localhost:8080/actuator/health` returns `{"status":"UP"}`
- ✅ Check that `http://localhost:8080/actuator/info` returns your custom application info

### 2. Application Functionality
- ✅ Verify that `http://localhost:8080/hello` returns "Hello, Spring Boot Actuator!"
- ✅ Confirm the application starts without errors

### 3. Expected Output Examples

**/actuator response:**
```json
{
  "_links": {
    "self": {
      "href": "http://localhost:8080/actuator",
      "templated": false
    },
    "health": {
      "href": "http://localhost:8080/actuator/health",
      "templated": false
    },
    "info": {
      "href": "http://localhost:8080/actuator/info",
      "templated": false
    }
  }
}
```

**/actuator/health response:**
```json
{
  "status": "UP",
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 419430400000,
        "threshold": 10485760
      }
    }
  }
}
```

## Reflection Questions

1. **What is the purpose of the `/actuator` endpoint itself?**
   - It serves as a discovery endpoint, showing all available actuator endpoints and their URLs

2. **Why do you think Spring Boot doesn't expose all Actuator endpoints by default?**
   - Security reasons - some endpoints expose sensitive information
   - Performance considerations - not all endpoints are needed in production
   - Configuration flexibility - allows developers to choose what to expose

3. **What is the significance of the "UP" status for the `/health` endpoint?**
   - Indicates the application is running and healthy
   - Used by load balancers and monitoring systems to determine if the service is available
   - Critical for microservice architectures and container orchestration

4. **How could you use the `/info` endpoint in a production environment?**
   - Display application version information
   - Show build details and deployment information
   - Provide contact information for the development team

## Key Concepts Covered

- **Spring Boot Actuator**: Production-ready features for monitoring and managing applications
- **spring-boot-starter-actuator**: Starter dependency that includes actuator functionality
- **Actuator Endpoints**: `/actuator`, `/health`, `/info`, `/metrics`, `/env`, `/configprops`
- **Application Health Status**: UP/DOWN status indicating application health
- **Endpoint Exposure**: Configuring which endpoints are available via HTTP
- **Health Indicators**: Built-in health checks for various components
- **Application Properties**: Using `application.properties` to configure actuator behavior

## Resources

- [Spring Boot Actuator Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Baeldung: Spring Boot Actuators](https://www.baeldung.com/spring-boot-actuators)
- [Spring Boot Actuator Endpoints Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)
- [Spring Boot Health Indicators](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.health)

## Next Steps
After completing this lab, you should be comfortable with:
- Enabling Spring Boot Actuator
- Accessing basic endpoints
- Understanding the health status concept
- Configuring basic actuator properties

This foundation will prepare you for more advanced actuator features in subsequent labs. 