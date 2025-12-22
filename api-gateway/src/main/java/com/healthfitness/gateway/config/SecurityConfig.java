package com.healthfitness.gateway.config;

import com.healthfitness.gateway.service.UserDataPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class SecurityConfig {

    private final UserDataPublisher userDataPublisher;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    public SecurityConfig(UserDataPublisher userDataPublisher) {
        this.userDataPublisher = userDataPublisher;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/actuator/**").hasAuthority("easy-fitness.admin")
                        .pathMatchers("/api/cardio/**").hasAnyAuthority("easy-fitness.admin", "easy-fitness.user")
                        .pathMatchers("/login/**", "/oauth2/**", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        // Removed .oauth2Login(oauth2 -> {}) as it conflicts with resource server configuration
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder
                .withIssuerLocation(issuerUri)
                .build();
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

            // Publish user data to Kafka
            try {
                userDataPublisher.publishUserData(jwt);
            } catch (Exception e) {
                log.error("Error publishing user data to Kafka", e);
            }

            // Конвертируем в токен аутентификации
            AbstractAuthenticationToken authToken = converter.convert(jwt);
            return Mono.just(authToken);
        };
    }
}


