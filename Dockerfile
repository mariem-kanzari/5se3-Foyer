
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

# Use a base Java image
FROM openjdk:17-jdk-slim

# Metadata
LABEL authors="ghofrane"

# Set the working directory
WORKDIR /app

# Copy the generated JAR file into the container (update the JAR file name here if it changes)
ADD target/Projet-Devops.jar Projet-Devops.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/Projet-Devops"]

