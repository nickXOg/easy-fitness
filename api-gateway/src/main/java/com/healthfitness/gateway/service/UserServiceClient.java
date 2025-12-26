package com.healthfitness.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final WebClient.Builder webClientBuilder;

    /**
     * Проверяет, существует ли пользователь в user-service по keycloakId
     */
    public Mono<Boolean> doesUserExist(String keycloakId) {
        try {
            return webClientBuilder
                    .baseUrl("http://user-service") // используем Eureka service discovery
                    .build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/profile/by-keycloak-id/{keycloakId}")
                            .build(keycloakId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> true) // если запрос успешен, пользователь существует
                    .onErrorReturn(false); // если ошибка (например, 404), пользователь не существует
        } catch (Exception e) {
            log.error("Error checking if user exists", e);
            return Mono.just(false);
        }
    }
}