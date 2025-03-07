# Utilisation d'une image légère de OpenJDK 21
FROM openjdk:21-jdk-slim

# Définition du répertoire de travail
WORKDIR /app

# Copie du fichier JAR généré dans l'image
COPY target/*.jar app.jar

# Exposition du port de l'application
EXPOSE 8080

# Définition des variables d'environnement pour la configuration de l'application
ENV SPRING_APPLICATION_NAME=MPMT \
    SPRING_DATASOURCE_URL=jdbc:mysql://db:33061/mpmt \
    SPRING_DATASOURCE_USERNAME=mpmt \
    SPRING_DATASOURCE_PASSWORD=mpmt \
    SPRING_MAIL_HOST=mail \
    SPRING_MAIL_PORT=1025 \
    SPRING_MAIL_USERNAME=your-username \
    SPRING_MAIL_PASSWORD=your-password

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
