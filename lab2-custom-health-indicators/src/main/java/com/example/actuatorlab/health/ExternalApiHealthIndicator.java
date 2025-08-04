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
        // TODO 4: Initialize WebClient with base URL "https://httpbin.org"
        // TODO 5: Uncomment the following code:
        /*
        this.webClient = webClientBuilder
                .baseUrl("https://httpbin.org")
                .build();
        */
        
        // Placeholder initialization - replace with actual implementation
        this.webClient = webClientBuilder.build();
    }
    
    @Override
    public Health health() {
        // TODO 6: Implement external API health check
        // 1. Use webClient to make a GET request to "/status/200"
        // 2. Set a timeout of 5 seconds
        // 3. Return Health.up() with API details if successful
        // 4. Return Health.down() with error details if exception occurs
        
        try {
            // TODO 7: Uncomment the following code to implement the health check:
            /*
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
            */
            
            // Placeholder return - replace with actual implementation
            return Health.unknown()
                    .withDetail("external-api", "httpbin.org")
                    .withDetail("status", "Not implemented yet")
                    .build();
                    
        } catch (Exception e) {
            // TODO 8: Uncomment the following code to handle exceptions:
            /*
            return Health.down()
                    .withDetail("external-api", "httpbin.org")
                    .withDetail("error", e.getMessage())
                    .build();
            */
            
            // Placeholder return - replace with actual implementation
            return Health.down()
                    .withDetail("external-api", "httpbin.org")
                    .withDetail("error", "Health check not implemented")
                    .build();
        }
    }
} 