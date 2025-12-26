# Eureka Server

## Overview

The Eureka Server is a Spring Cloud application that provides service discovery for the Health & Fitness platform. All microservices register with this server, allowing them to locate each other and communicate dynamically without hardcoding hostnames and ports.

## Prerequisites

-   Java 21
-   Maven 3.x

## Configuration

The server configuration is located in `src/main/resources/application.yml`. The default settings configure the server to run on port `8761` and prevent it from registering with itself.

-   `server.port`: The port on which the Eureka server will run.
-   `eureka.client.register-with-eureka`: Set to `false` so the server does not register itself.
-   `eureka.client.fetch-registry`: Set to `false` to prevent the server from fetching the registry from another Eureka instance.

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
    java -jar target/eureka-server-1.0.0-SNAPSHOT.jar
    ```

## Eureka Dashboard

Once the server is running, you can access the Eureka dashboard in your web browser to view all registered service instances.

-   **URL:** `http://localhost:8761/`
