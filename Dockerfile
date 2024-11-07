# Base image with Java JDK 17 (slim version for a smaller image size)
FROM openjdk:17-jdk-slim
LABEL authors="shadha"

# Set the working directory inside the container
WORKDIR /app

# Expose the application port
EXPOSE 8082

# Copy the built JAR file from the host to the container
COPY target/Projet-Devops-0.2.0.jar tp-foyer.jar

# Run the application
ENTRYPOINT ["java", "-jar", "tp-foyer.jar"]
