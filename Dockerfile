# Use a base Java image
FROM openjdk:17-jre-slim

# Metadata
LABEL authors="ghofrane"

# Set the working directory
WORKDIR /app

# Copy the generated JAR file into the container (update the JAR file name here if it changes)
COPY target/Projet-Devops-0.0.2-SNAPSHOT.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
