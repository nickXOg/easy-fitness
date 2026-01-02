package com.healthfitness.gateway.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Check if this is a Google-issued token
        String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
        if (issuer != null && issuer.contains("accounts.google.com")) {
            // Handle Google OAuth2 token
            return handleGoogleToken(jwt);
        } else {
            // Handle Keycloak-issued token (original logic)
            return handleKeycloakToken(jwt);
        }
    }

    private Collection<GrantedAuthority> handleKeycloakToken(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || roles.isEmpty()) {
            // Try to get roles from the 'realm_access' claim
            Object realmAccessObj = jwt.getClaim("realm_access");
            if (realmAccessObj instanceof Map) {
                Map<String, Object> realmAccess = (Map<String, Object>) realmAccessObj;
                Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof List) {
                    roles = (List<String>) rolesObj;
                }
            }
        }

        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Collection<GrantedAuthority> handleGoogleToken(Jwt jwt) {
        // For Google authentication, we'll assign default roles
        // In a production system, you might want to implement custom logic here
        return Collections.singletonList(new SimpleGrantedAuthority("easy-fitness.user"));
    }
}
