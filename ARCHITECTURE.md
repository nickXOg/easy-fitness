# Health & Fitness Platform Architecture

## Overview
The Health & Fitness Platform is a microservices-based application built with Spring Boot, Spring Cloud, and Java 21. It follows a service-oriented architecture with multiple interconnected services that handle different aspects of a fitness tracking platform.

## Architecture Components

### 1. Eureka Server (`eureka-server`)
- **Purpose**: Service registry and discovery mechanism
- **Port**: 8761
- **Technology**: Spring Cloud Netflix Eureka Server
- **Configuration**:
  - Self-registered (not registering with other Eureka servers)
  - Not fetching registry (standalone mode)
  - Service URL: http://localhost:8761/eureka/
- **Role**: Provides service discovery capabilities, enabling dynamic service registration and lookup

### 2. API Gateway (`api-gateway`)
- **Purpose**: Centralized entry point that routes requests to appropriate services
- **Port**: 8000
- **Technology**: Spring Cloud Gateway with WebFlux
- **Configuration**:
  - Routes to cardio-service for `/api/cardio/**` paths
  - Routes to user-service for `/api/users/**` paths
  - Integrates with OAuth2 resource server for security
  - Uses service discovery (Eureka) for load balancing
- **Security**: JWT-based authentication and authorization using Keycloak
- **Additional Features**:
  - Kafka integration for user data events
  - Actuator endpoints for monitoring
  - OAuth2 client registration for Keycloak and Google

### 3. User Service (`user-service`)
- **Alternative Name**: Auth Service (handles authentication and authorization)
- **Port**: 8081
- **Technology**: Spring Boot with Spring Security and JWT
- **Database**: PostgreSQL (user_service database)
- **Configuration**:
  - OAuth2 resource server with JWT validation
  - Kafka consumer for user data events
  - Database migration with Flyway
  - JPA with PostgreSQL for data persistence
- **Role**: Handles user authentication, authorization, and user data management
- **Security**: JWT-based security with Keycloak as the identity provider

### 4. Cardio Service (`cardio-service`)
- **Purpose**: Manages cardio workout tracking and related data
- **Port**: 8002
- **Technology**: Spring Boot with Spring Data JPA
- **Database**: PostgreSQL (cardio_service database)
- **Configuration**:
  - REST API for creating and retrieving workout records
  - JPA with PostgreSQL for data persistence
  - Kafka integration for event streaming
  - OAuth2 resource server for security
- **Role**: Handles cardio workout data management
- **API Endpoints**:
  - POST /api/cardio/workouts - Create a new cardio workout
  - GET /api/cardio/workouts/user/{userId} - Retrieve all cardio workouts for a user

## Infrastructure Components

### 1. Docker Compose Services
- **Redis** (port 6379): Caching service
- **Zookeeper** (port 2181): Kafka coordination service
- **Kafka** (port 9092): Event streaming and messaging platform
- **Kafka UI** (port 8180): Web-based Kafka management interface
- **Keycloak** (port 8080): Identity management and OAuth2 provider

### 2. Databases
- **PostgreSQL**: Primary database for persistent data storage
  - user_service database for user service
  - cardio_service database for cardio service

### 3. Security
- **OAuth2/JWT**: For authentication and authorization
- **Keycloak**: Identity management system
- **Google OAuth2**: Social login integration

### 4. Additional Components
- **Flyway**: Database migration management
- **Maven**: Dependency management and builds
- **Docker & Docker Compose**: Containerization and orchestration

## Technology Stack

### Backend Technologies
- **Java 21**: Minimum required version
- **Spring Boot 3.2.0**: Framework for building microservices
- **Spring Cloud 2023.0.0**: Cloud-native patterns and services
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database access and ORM
- **Spring Cloud Gateway**: API Gateway functionality
- **Spring Cloud Netflix Eureka**: Service discovery

### Infrastructure Technologies
- **PostgreSQL**: Relational database management system
- **Redis**: In-memory data structure store
- **Apache Kafka**: Distributed event streaming platform
- **ZooKeeper**: Centralized service for maintaining configuration
- **Keycloak**: Identity and access management solution

### Development Tools
- **Lombok**: Java annotation library to reduce boilerplate code
- **MapStruct**: Code generator for Java bean mappings
- **Maven**: Build automation and dependency management
- **Docker & Docker Compose**: Containerization and orchestration

## Communication Patterns

### Synchronous Communication
- REST APIs using HTTP between services via API Gateway
- Direct database access using JPA

### Asynchronous Communication
- Apache Kafka for event streaming between services
- Event-driven architecture for user data and workout events

## Security Implementation
- JWT tokens for authentication and authorization
- OAuth2 resource server configuration in each service
- Keycloak as the centralized identity provider
- OAuth2 client registration in API Gateway for both Keycloak and Google

## Development and Deployment

### Local Development Setup
1. Start external dependencies with Docker Compose
2. Build the application with Maven
3. Run services in order: Eureka Server → User Service → Cardio Service → API Gateway

### Production Deployment
- Each service can be packaged as a JAR file and deployed independently
- Docker containers can be built and deployed using provided Dockerfiles
- Services automatically register with Eureka for service discovery

## Testing
- Unit and integration tests run using Maven
- Each service has its own test suite

## Monitoring and Observability
- Actuator endpoints for health checks and metrics
- Kafka UI for monitoring event streams
- Detailed logging configuration for debugging security flows