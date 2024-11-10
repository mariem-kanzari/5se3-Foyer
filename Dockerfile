

FROM openjdk:17-alpine


WORKDIR /app


COPY pom.xml .
COPY src ./src



COPY target/Projet-Devops-0.0.4.jar app.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]

