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
    
    // TODO 5: Add a map to store feature flags
    // TODO 6: Uncomment the following code:
    /*
    private final Map<String, Boolean> featureFlags = new HashMap<>();
    
    public FeatureFlagsEndpoint() {
        // Initialize with some default feature flags
        featureFlags.put("new-ui", false);
        featureFlags.put("beta-features", true);
        featureFlags.put("debug-mode", false);
    }
    */
    
    // Placeholder field - replace with actual implementation
    private final Map<String, Boolean> featureFlags = new HashMap<>();
    
    @ReadOperation
    public Map<String, Boolean> getFeatureFlags() {
        // TODO 7: Implement the getFeatureFlags method
        // Return a copy of the feature flags map
        
        // TODO 8: Uncomment the following code:
        /*
        return new HashMap<>(featureFlags);
        */
        
        // Placeholder return - replace with actual implementation
        Map<String, Boolean> placeholderFlags = new HashMap<>();
        placeholderFlags.put("status", false);
        return placeholderFlags;
    }
    
    @WriteOperation
    public void updateFeatureFlag(String name, Boolean enabled) {
        // TODO 9: Implement the updateFeatureFlag method
        // 1. Validate that name and enabled are not null
        // 2. Update the feature flag in the map
        
        // TODO 10: Uncomment the following code:
        /*
        if (name != null && enabled != null) {
            featureFlags.put(name, enabled);
        }
        */
    }
} 