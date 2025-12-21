package com.healthfitness.cardio.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // 1) Лог всего payload токена
        log.info("JWT claims: {}", jwt.getClaims());
        // 2) Лог отдельных клеймов, которые тебе важны
        log.info("sub: {}, preferred_username: {}, realm_access: {}, resource_access: {}",
                jwt.getSubject(),
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaim("realm_access"),
                jwt.getClaim("resource_access")
        );
        Map<String, Object> realm_access = jwt.getClaim("realm_access");
        if (realm_access != null) {
            List<String> roles = (List<String>) realm_access.get("roles");
            if (roles != null) {
                // Добавляем префикс ROLE_ к каждой роли
                return roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
