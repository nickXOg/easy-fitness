package com.healthfitness.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class ApiGatewayIntegrationTest {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Mock external service URLs to avoid actual connections during tests
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> "http://localhost:8080/realms/easy-fitness");
        registry.add("eureka.client.serviceUrl.defaultZone", () -> "http://localhost:8761/eureka/");
    }

    @Test
    void contextLoads() {
        // This test verifies that the application context loads successfully with our configuration
        // It ensures there are no configuration conflicts in our security setup
    }
}