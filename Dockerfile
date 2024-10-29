# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Maven build files and source code
COPY pom.xml .
COPY src ./src

# Copy the built JAR file to the container
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
