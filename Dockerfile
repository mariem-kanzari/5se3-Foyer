# Utiliser une image de base Java
FROM openjdk:17-jre-slim

LABEL authors="ghofrane"
# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR dans le conteneur
COPY target/0.0.2-SNAPSHOT.jar app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Commande pour exécuter l'application
CMD ["java", "-jar", "app.jar"]
