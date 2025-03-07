# Utiliser l'image officielle OpenJDK 21 comme base
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré dans le conteneur
COPY target/*.jar app.jar

# Exposer le port sur lequel l'application écoute (modifie si nécessaire)
EXPOSE 8080

# Définir la commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]