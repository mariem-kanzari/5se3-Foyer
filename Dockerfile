# Use JDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the downloaded artifact from Jenkins workspace into the container
COPY target/Projet-Devops.jar foyer.jar

# Expose the port your application runs on
EXPOSE 8083

# Command to run your application
CMD ["java", "-jar", "foyer.jar"]
