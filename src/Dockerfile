# Use the official OpenJDK 17 image from the Docker Hub
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Define a build argument for the JAR file name
ARG JAR_FILE

# Copy the JAR file into the container using the build argument
COPY ${JAR_FILE} app.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
