package com.healthfitness.auth.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    public JwtAuthConverter() {
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Check if this is a Google-issued token
        String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
        Collection<GrantedAuthority> authorities;

        if (issuer != null && issuer.contains("accounts.google.com")) {
            // Handle Google OAuth2 token
            authorities = handleGoogleToken(jwt);
        } else {
            // Handle Keycloak-issued token (original logic)
            authorities = handleKeycloakToken(jwt);
        }

        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = "preferred_username"; // Default to preferred_username
        // You can add logic here to determine the principal claim name based on your Keycloak setup
        return jwt.getClaimAsString(claimName);
    }

    private Collection<GrantedAuthority> handleKeycloakToken(Jwt jwt) {
        return Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream())
                .collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> handleGoogleToken(Jwt jwt) {
        // For Google authentication, assign default roles
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_easy-fitness.user"));
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            // Try to get roles from the 'realm_access' claim
            Object realmAccessObj = jwt.getClaim("realm_access");
            if (realmAccessObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> realmAccessMap = (Map<String, Object>) realmAccessObj;
                Object rolesObj = realmAccessMap.get("roles");
                if (rolesObj instanceof List) {
                    List<String> roles = (List<String>) rolesObj;
                    if (roles != null) {
                        return roles.stream()
                                .map(role -> "ROLE_" + role)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet());
                    }
                }
            }
            return Collections.emptySet();
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
