package com.healthfitness.gateway.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KeycloakJwtAuthenticationConverterTest {

    private KeycloakJwtAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new KeycloakJwtAuthenticationConverter();
    }

    @Test
    void shouldConvertKeycloakTokenWithRoles() throws Exception {
        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getIssuer()).thenReturn(new java.net.URL("http://localhost:8080/realms/easy-fitness"));
        when(mockJwt.getClaimAsStringList("roles")).thenReturn(java.util.Arrays.asList("user", "admin"));

        Collection<GrantedAuthority> authorities = converter.convert(mockJwt);

        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("user")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("admin")));
    }

    @Test
    void shouldConvertGoogleToken() throws Exception {
        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getIssuer()).thenReturn(new java.net.URL("https://accounts.google.com"));

        Collection<GrantedAuthority> authorities = converter.convert(mockJwt);

        assertEquals(1, authorities.size());
        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("easy-fitness.user", authority.getAuthority());
    }

    @Test
    void shouldHandleKeycloakTokenWithoutRoles() throws Exception {
        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getIssuer()).thenReturn(new java.net.URL("http://localhost:8080/realms/easy-fitness"));
        when(mockJwt.getClaimAsStringList("roles")).thenReturn(null); // Return null for roles

        Collection<GrantedAuthority> authorities = converter.convert(mockJwt);

        assertTrue(authorities.isEmpty());
    }
}