

FROM openjdk:17-alpine


WORKDIR /app


COPY pom.xml .
COPY src ./src



COPY target/Projet-Devops-0.0.4.jar Projet-Devops-0.0.4.jar


EXPOSE 8087


ENTRYPOINT ["java", "-jar", "Projet-Devops-0.0.4.jar"]

