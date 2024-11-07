FROM openjdk:17-jdk-alpine AS downloader

# Installer curl
RUN apk add --no-cache curl

# Définir l'URL du JAR dans une variable d'environnement
ENV JAR_URL=http://192.168.33.10:8081/repository/maven-releases/com/example/Projet-Devops/0.1.0/Projet-Devops-0.1.0.jar

# Télécharger le JAR depuis Nexus
RUN curl -u admin:esprit -o /Projet-Devops-0.1.0.jar $JAR_URL

# Étape 2: Construire l'image finale
FROM openjdk:17-jdk-alpine
EXPOSE 8089

# Copier le JAR téléchargé depuis l'étape downloader
COPY --from=downloader /Projet-Devops-0.1.0.jar Projet-Devops-0.1.0.jar

ENTRYPOINT ["java", "-jar", "Projet-Devops-0.1.0.jar"]








