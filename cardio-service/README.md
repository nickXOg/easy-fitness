# Cardio Service

## Overview

The Cardio Service is a Spring Boot microservice responsible for managing cardio workout data within the Health & Fitness platform. It allows users to create and retrieve cardio workout records. The service integrates with Keycloak for security, uses a PostgreSQL database for persistence, and registers with Eureka for service discovery. It also communicates with other services via Kafka for event-driven messaging.

## Prerequisites

Before running the service, ensure you have the following installed and running:
-   Java 21
-   Maven 3.x
-   PostgreSQL
-   Keycloak
-   Eureka Server
-   Apache Kafka

## Configuration

The service configuration is located in `src/main/resources/application.yml`. You may need to update the following properties to match your environment:

-   **Database:**
    -   `spring.datasource.url`
    -   `spring.datasource.username`
    -   `spring.datasource.password`
-   **Keycloak:**
    -   `spring.security.oauth2.resourceserver.jwt.issuer-uri`
    -   `spring.security.oauth2.client.registration.keycloak.*`
-   **Kafka:**
    -   `spring.cloud.stream.kafka.binder.brokers`
-   **Eureka:**
    -   `eureka.client.serviceUrl.defaultZone`

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
    java -jar target/cardio-service-1.0.0-SNAPSHOT.jar
    ```

## API Documentation

The service exposes the following RESTful endpoints:

### Cardio Workout Endpoints

-   **Create a New Workout**
    -   **URL:** `/api/cardio/workouts`
    -   **Method:** `POST`
    -   **Description:** Creates a new cardio workout record.
    -   **Request Body:**
        ```json
        {
          "userId": 1,
          "workoutType": "Running",
          "duration": 30,
          "distance": 5.0,
          "calories": 300,
          "workoutDate": "2024-12-25T10:00:00"
        }
        ```
    -   **Success Response:**
        -   **Code:** 200 OK
        -   **Content:** The created `CardioWorkoutDTO` object.

-   **Get User Workouts**
    -   **URL:** `/api/cardio/workouts/user/{userId}`
    -   **Method:** `GET`
    -   **Description:** Retrieves a list of all cardio workouts for a specific user.
    -   **URL Parameters:**
        -   `userId=[integer]` (required) - The ID of the user.
    -   **Success Response:**
        -   **Code:** 200 OK
        -   **Content:** A list of `CardioWorkoutDTO` objects.
