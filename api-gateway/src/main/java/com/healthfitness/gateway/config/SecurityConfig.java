package com.healthfitness.gateway.config;

import com.healthfitness.gateway.client.UserServiceClient;
import com.healthfitness.gateway.keycloak.KeycloakJwtAuthenticationConverter;
import com.healthfitness.gateway.message.UserDataMessage;
import com.healthfitness.gateway.service.UserDataPublisher;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {
    private final UserDataPublisher userDataPublisher;
    private final UserServiceClient userServiceClient;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

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
        return jwt -> {
            // Проверяем существование пользователя и отправляем данные в Kafka
            log.info("Checking if user exists with keycloakId: {}", jwt.getSubject());
            return userServiceClient.doesUserExist(jwt.getSubject())
                    .flatMap(userExists -> {
                        log.info("User exists check result: {}", userExists);
                        if (Boolean.FALSE.equals(userExists)) {
                            // Publish user data to Kafka
                            log.info("User does not exist, publishing user data to Kafka for keycloakId: {}", jwt.getSubject());
                            try {
                                UserDataMessage userDataMessage = UserDataMessage.builder()
                                        .keycloakId(jwt.getSubject())
                                        .email(jwt.getClaimAsString("email"))
                                        .firstName(jwt.getClaimAsString("given_name"))
                                        .lastName(jwt.getClaimAsString("family_name"))
                                        .build();
                                // Используем fromRunnable для асинхронной отправки в Kafka
                                return Mono.fromRunnable(() -> {
                                    try {
                                        userDataPublisher.publishUserData(userDataMessage);
                                        log.info("Published user data to Kafka for keycloakId: {}", jwt.getSubject());
                                    } catch (Exception e) {
                                        log.error("Error publishing user data to Kafka", e);
                                    }
                                }).then(Mono.just((delegate).convert(jwt)));
                            } catch (Exception e) {
                                log.error("Error building user data message", e);
                                return Mono.just((delegate).convert(jwt));
                            }
                        } else {
                            // Пользователь существует, просто конвертируем JWT
                            log.info("User exists, skipping Kafka publish for keycloakId: {}", jwt.getSubject());
                            return Mono.just((delegate).convert(jwt));
                        }
                    });
        };
    }
}
