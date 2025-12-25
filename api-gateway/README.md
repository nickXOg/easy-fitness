# API Gateway

## Overview

The API Gateway is a Spring Cloud Gateway application that serves as the single entry point for all client requests to the Health & Fitness platform's microservices. It is responsible for routing requests to the appropriate services, as well as handling cross-cutting concerns such as security, monitoring, and resilience.

The gateway is integrated with Eureka for service discovery and Keycloak for OAuth2/JWT-based authentication and authorization.

## Prerequisites

Before running the gateway, ensure you have the following installed and running:
-   Java 21
-   Maven 3.x
-   Keycloak
-   Eureka Server
-   All backend microservices (e.g., User Service, Cardio Service)

## Configuration

The gateway configuration is located in `src/main/resources/application.yml`. You may need to update the following properties to match your environment:

-   **Keycloak:**
    -   `spring.security.oauth2.client.registration.keycloak.*`
    -   `spring.security.oauth2.client.provider.keycloak.issuer-uri`
    -   `spring.security.oauth2.resourceserver.jwt.issuer-uri`
-   **Google OAuth2 (Optional):**
    -   `spring.security.oauth2.client.registration.google.*`
-   **Eureka:**
    -   `eureka.client.serviceUrl.defaultZone`

### Route Configuration

Routes are defined in `application.yml` under `spring.cloud.gateway.routes`. The gateway uses service discovery (`lb://service-name`) to route requests to downstream services.

-   **Cardio Service:**
    -   `id: cardio-service`
    -   `uri: lb://cardio-service`
    -   `predicates: Path=/api/cardio/**`
-   **User Service:**
    -   `id: user-service`
    -   `uri: lb://user-service`
    -   `predicates: Path=/api/users/**`

## How to Build and Run

1.  **Build the project:**
    ```bash
    mvn clean install
    ```

2.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the packaged JAR file:
    ```bash
    java -jar target/api-gateway-1.0.0-SNAPSHOT.jar
    ```

## Security

The API Gateway secures all downstream services by enforcing JWT validation. All requests to protected routes must include a valid JWT token from the configured Keycloak issuer in the `Authorization` header. The security configuration is defined in `com.healthfitness.gateway.config.SecurityConfig`.
