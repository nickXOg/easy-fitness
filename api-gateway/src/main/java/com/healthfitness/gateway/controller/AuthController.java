package com.healthfitness.gateway.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @GetMapping("/oauth2/success")
    public Mono<Map<String, String>> oauth2Success(Authentication authentication, ServerWebExchange exchange) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // Get the registration ID from the authentication details
            String registrationId = "unknown";
            if (authentication.getDetails() instanceof org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) {
                registrationId = ((org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken) authentication.getDetails())
                    .getAuthorizedClientRegistrationId();
            }

            // Extract user attributes (works for both OIDC and regular OAuth2 users)
            String email = null;
            String name = null;

            if (oauth2User instanceof OidcUser) {
                OidcUser oidcUser = (OidcUser) oauth2User;
                email = oidcUser.getEmail();
                name = oidcUser.getName();
            } else {
                email = oauth2User.getAttribute("email");
                name = oauth2User.getAttribute("name");
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Authenticated successfully");
            response.put("email", email);
            response.put("name", name);
            response.put("registrationId", registrationId);

            return Mono.just(response);
        } else {
            throw new ResponseStatusException(
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                "Not authenticated via OAuth2"
            );
        }
    }

    @GetMapping("/login")
    public Mono<String> loginPage() {
        return Mono.just("Redirect to OAuth2 provider");
    }
}