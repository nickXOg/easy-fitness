package com.healthfitness.gateway.integration;

import com.healthfitness.gateway.config.KeycloakJwtAuthenticationConverter;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test to verify Google authentication token processing
 */
public class GoogleAuthenticationTest {

    private final KeycloakJwtAuthenticationConverter jwtConverter = new KeycloakJwtAuthenticationConverter();

    @Test
    public void testGoogleTokenProcessing() {
        // Create a mock Google JWT token with required claims
        Jwt googleToken = Jwt.withTokenValue("mock-google-token")
                .header("alg", "RS256")
                .claim("iss", "https://accounts.google.com")
                .claim("sub", "123456789")
                .claim("email", "test@example.com")
                .claim("given_name", "Test")
                .claim("family_name", "User")
                .build();

        // Process the token using our converter
        Collection<GrantedAuthority> authorities = jwtConverter.convert(googleToken);

        // Verify that the correct authorities are assigned for Google tokens
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> "easy-fitness.user".equals(auth.getAuthority())));
    }

    @Test
    public void testKeycloakTokenProcessing() {
        // Create a mock Keycloak JWT token with roles
        Jwt keycloakToken = Jwt.withTokenValue("mock-keycloak-token")
                .header("alg", "RS256")
                .claim("iss", "http://localhost:8080/realms/easy-fitness")
                .claim("sub", "123456789")
                .claim("email", "test@example.com")
                .claim("given_name", "Test")
                .claim("family_name", "User")
                .claim("realm_access",
                    java.util.Map.of("roles", java.util.List.of("easy-fitness.user", "easy-fitness.admin")))
                .build();

        // Process the token using our converter
        Collection<GrantedAuthority> authorities = jwtConverter.convert(keycloakToken);

        // Verify that roles from Keycloak token are properly converted
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> "easy-fitness.user".equals(auth.getAuthority())));
        assertTrue(authorities.stream()
                .anyMatch(auth -> "easy-fitness.admin".equals(auth.getAuthority())));
    }
}