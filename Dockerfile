# Use the official OpenJDK 17 image from the Docker Hub
FROM openjdk:17-jdk-slim
LABEL authors="mkanz"

# Set the working directory in the container
WORKDIR /app

# Build argument for the JAR file
ARG JAR_FILE

# Copy the downloaded JAR file into the container using the build argument
COPY ${JAR_FILE} app.jar

# Expose the port your application runs on (adjust if needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
