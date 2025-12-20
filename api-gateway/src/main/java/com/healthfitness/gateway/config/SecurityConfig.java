package com.healthfitness.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Configuration
@Slf4j
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/actuator/**").hasAuthority("ROLE_easy-fitness.admin")
                        .pathMatchers("/api/cardio/**").hasAnyAuthority("ROLE_easy-fitness.admin", "ROLE_easy-fitness.user")
                        .pathMatchers("/login/**", "/oauth2/**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .oauth2Login(oauth2 -> {
                });
        return http.build();
    }
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthenticationConverter());

        Converter<Jwt, AbstractAuthenticationToken> converter = delegate::convert;

        return jwt -> {
            // Логируем детали JWT
            log.info("===== ПОЛУЧЕН JWT ТОКЕН =====");
            log.info("Значение токена: {}", jwt.getTokenValue());
            log.info("Заголовки: {}", jwt.getHeaders());
            log.info("Клеймы (Claims): {}", jwt.getClaims());
            log.info("Время истечения: {}", jwt.getExpiresAt());
            log.info("Субъект (subject): {}", jwt.getSubject());
            log.info("Издатель (issuer): {}", jwt.getIssuer());
            log.info("===== КОНЕЦ JWT ЛОГА =====");

            // Конвертируем в токен аутентификации
            AbstractAuthenticationToken authToken = converter.convert(jwt);
            return Mono.just(authToken);
        };
    }
}


