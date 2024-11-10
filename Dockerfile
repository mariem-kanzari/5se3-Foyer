
# Use a base Java image
FROM openjdk:17-jdk-slim

# Metadata
LABEL authors="ghofrane"

# Set the working directory
WORKDIR /app

# Copy the generated JAR file into the container (update the JAR file name here if it changes)
ADD target/Projet-Devops.jar Projet-Devops.jar

# Expose the port on which the application will run
EXPOSE 8082

# Command to run the application
CMD ["java", "-jar", "/Projet-Devops"]

