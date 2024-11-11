FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven in the runtime container
RUN apt-get update && apt-get install -y maven

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
