# Use Java 17 runtime (lightweight Alpine version)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the Spring Boot jar built by Maven
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Render default)
EXPOSE 8080

# Use environment variables for DB if needed (optional for Dockerfile)
# They will be set in Render dashboard, so no change here

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
