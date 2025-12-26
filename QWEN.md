# Health & Fitness Platform

## Project Overview

Health & Fitness Platform is a microservices-based application built with Spring Boot, Spring Cloud, and Java 21. It follows a service-oriented architecture with multiple interconnected services that handle different aspects of a fitness tracking platform.

### Architecture

The platform consists of the following services:

- **Eureka Server** (`eureka-server`): Service registry and discovery mechanism
- **API Gateway** (`api-gateway`): Centralized entry point that routes requests to appropriate services
- **User Service** (`user-service`): Authentication and authorization service (named auth-service in documentation)
- **Cardio Service** (`cardio-service`): Manages cardio workout tracking and related data
- **Infrastructure**: Docker Compose manages external dependencies like Redis, Kafka, Zookeeper, and Keycloak

### Technologies Used

- **Java 21** (minimum)
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **PostgreSQL** for persistent data storage
- **Redis** for caching
- **Apache Kafka & ZooKeeper** for event streaming and messaging
- **Keycloak** for identity management
- **OAuth2/JWT** for security
- **Flyway** for database migrations
- **Maven** for dependency management and builds
- **Docker & Docker Compose** for containerization and orchestration

### Services Details

#### User Service (Auth Service)
- Handles user authentication and authorization
- Uses PostgreSQL for user data storage
- Implements JWT-based security
- Provides registration and login endpoints

#### Cardio Service
- Manages cardio workout data
- REST API for creating and retrieving workout records
- Uses JPA with PostgreSQL for data persistence
- Integrates with Kafka for event streaming
- Implements OAuth2 resource server for security

#### API Gateway
- Routes requests to appropriate microservices
- Implements security layer with JWT validation
- Built on Spring Cloud Gateway with WebFlux
- Integrates with OAuth2 resource server

#### Eureka Server
- Provides service discovery capabilities
- Enables dynamic service registration and lookup

## Building and Running

### Prerequisites
- Java 21+
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL 12+

### Local Development Setup

1. **Start External Dependencies**:
   ```bash
   docker-compose up -d
   ```

2. **Build the Application**:
   ```bash
   mvn clean package
   ```

3. **Run Services in Order**:
   - First, start Eureka Server:
     ```bash
     cd eureka-server
     mvn spring-boot:run
     ```
   
   - Then, start User Service:
     ```bash
     cd ../user-service  
     mvn spring-boot:run
     ```
   
   - Then, start Cardio Service:
     ```bash
     cd ../cardio-service
     mvn spring-boot:run
     ```
   
   - Finally, start API Gateway:
     ```bash
     cd ../api-gateway
     mvn spring-boot:run
     ```

### Production Deployment
Each service can be packaged as a JAR file and deployed independently:
```bash
mvn clean package
java -jar target/service-name.jar
```

Alternatively, Docker containers can be built and deployed using the provided Dockerfiles.

## API Documentation

The Cardio Service API is documented in OpenAPI 3.0 format (see `cardio-service/openapi.yaml`):
- `POST /api/cardio/workouts` - Create a new cardio workout
- `GET /api/cardio/workouts/user/{userId}` - Retrieve all cardio workouts for a user

## Development Conventions

- Each service resides in its own module directory
- Configuration is handled via `application.yml` files
- Database migrations are managed by Flyway
- Code uses Lombok and MapStruct for reducing boilerplate
- Security is implemented using Spring Security with OAuth2/JWT
- Kafka is used for asynchronous communication between services

## Testing

Unit and integration tests are run using Maven:
```bash
mvn test
```

## Infrastructure

The `docker-compose.yml` file sets up the following infrastructure services:
- Redis (port 6379) - Caching
- Zookeeper (port 2181) - Kafka coordination
- Kafka (port 9092) - Event streaming
- Keycloak (port 8080) - Identity management

## Database Schema

Each service manages its own database schema using Flyway migrations. See individual service directories for migration scripts in `src/main/resources/db/migration/`.