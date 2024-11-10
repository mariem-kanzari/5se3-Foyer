

FROM openjdk:17-alpine


WORKDIR /app

COPY ./target/Projet-Devops.jar /app/Projet-Devops.jar






EXPOSE 8087







CMD ["java", "-jar", "/app/Projet-Devops.jar"]
