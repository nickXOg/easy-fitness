# User Service

## Overview

The User Service is a Spring Boot microservice responsible for managing user-related data and authentication within the Health & Fitness platform. It integrates with Keycloak for identity and access management, uses a PostgreSQL database for data persistence, and registers with the Eureka server for service discovery.

## Prerequisites

Before running the service, ensure you have the following installed and running:
-   Java 21
-   Maven 3.x
-   PostgreSQL
-   Keycloak
-   Eureka Server

## Configuration

The service configuration is located in `src/main/resources/application.yml`. You may need to update the following properties to match your environment:

-   **Database:**
    -   `spring.datasource.url`
    -   `spring.datasource.username`
    -   `spring.datasource.password`
-   **Keycloak:**
    -   `spring.security.oauth2.resourceserver.jwt.issuer-uri`
-   **Eureka:**
    -   `eureka.client.service-url.defaultZone`

The service uses Flyway for database migrations. Schema definitions can be found in `src/main/resources/db/migration`.

## How to Build and Run

1.  **Build the project:**
    ```bash
    mvn clean install
    ```

2.  **Run the application:**
    You can run the service using the Spring Boot Maven plugin:
    ```bash
    mvn spring-boot:run
    ```
    Alternatively, you can run the packaged JAR file:
    ```bash
    java -jar target/user-service-1.0.0-SNAPSHOT.jar
    ```

## API Documentation

The service exposes the following RESTful endpoints:

### User Endpoints

-   **Get Current User**
    -   **URL:** `/api/users/me`
    -   **Method:** `GET`
    -   **Description:** Retrieves the details of the currently authenticated user. Requires a valid JWT token in the Authorization header.
    -   **Success Response:**
        -   **Code:** 200 OK
        -   **Content:**
            ```json
            {
              "id": "uuid-string",
              "username": "testuser",
              "email": "test@example.com",
              "firstName": "Test",
              "lastName": "User"
            }
            ```
