# First stage: Build the JAR with Maven
FROM maven:3.8-openjdk-17-slim AS build

WORKDIR /app

# Copy your pom.xml and source code
COPY pom.xml .
COPY src ./src

# Run Maven to build the JAR
RUN mvn clean package -DskipTests

# Second stage: Run the JAR file with OpenJDK 17
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
