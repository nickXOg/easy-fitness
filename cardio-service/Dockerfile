# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-jammy AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory in the container to /app
WORKDIR /app

# Copy the entire Maven project from the build context (root of easy-fitness)
COPY . .

# Set the working directory to the specific module and build it
WORKDIR /app/cardio-service
RUN mvn clean package -DskipTests

# Stage 2: Create the final lean image
FROM eclipse-temurin:21-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the fat jar from the builder stage
COPY --from=builder /app/cardio-service/target/*.jar app.jar

# Make port 8002 available to the world outside this container
EXPOSE 8002

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]