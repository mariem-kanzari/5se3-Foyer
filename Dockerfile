

FROM openjdk:17-alpine


WORKDIR /app


COPY pom.xml .
COPY src ./src



COPY target/myapp.jar app.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]

