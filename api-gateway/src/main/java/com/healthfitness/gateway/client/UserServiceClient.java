package com.healthfitness.gateway.client;

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
        return webClientBuilder
                .baseUrl("http://user-service")
                .build()
                .get()
                .uri("/api/users/profile/by-keycloak-id/{keycloakId}", keycloakId)
                .exchangeToMono(response ->
                        Mono.just(switch (response.statusCode().value()) {
                            case 404 -> false;
                            default -> true;
                        })
                )
                .onErrorReturn(false);
    }
}