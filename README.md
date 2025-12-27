# Health & Fitness Platform

## Overview

This repository contains the source code for the Health & Fitness platform, a microservices-based application designed to help users track their fitness activities. The platform is built using Java, Spring Boot, and other modern technologies.

## Architecture

The platform follows a microservices architecture, with the following key components:

-   **Eureka Server:** Provides service discovery for all microservices in the platform.
-   **API Gateway:** A single entry point for all client requests, responsible for routing, security, and other cross-cutting concerns.
-   **User Service:** Manages user data and authentication.
-   **Cardio Service:** Manages cardio workout data.

## Technologies

-   **Backend:** Java 21, Spring Boot 3
-   **Security:** Spring Security, OAuth2, Keycloak
-   **Database:** PostgreSQL, Flyway
-   **Messaging:** Apache Kafka, Spring Cloud Stream
-   **Service Discovery:** Netflix Eureka
-   **API Gateway:** Spring Cloud Gateway
-   **Build Tool:** Apache Maven

## Prerequisites

To build and run the entire platform, you will need the following installed:

-   Java 21
-   Maven 3.x
-   Docker and Docker Compose
-   PostgreSQL
-   Keycloak
-   Apache Kafka

## How to Run the Platform

The easiest way to run the entire platform is by using the provided `docker-compose.yml` file. This will start all the necessary infrastructure components and microservices.

```bash
docker-compose up -d
```

For more detailed instructions on how to build and run each individual service, please refer to the `README.md` file in each service's directory.

## Microservices

-   [**API Gateway**](./api-gateway/README.md)
-   [**Cardio Service**](./cardio-service/README.md)
-   [**Eureka Server**](./eureka-server/README.md)
-   [**User Service**](./user-service/README.md)
