

FROM openjdk:17-alpine


WORKDIR /app

COPY ./target/Projet-Devops-0.0.4.jar /app/Projet-Devops-0.0.4.jar






EXPOSE 8087







CMD ["java", "-jar", "/app/Projet-Devops-0.0.4.jar"]
