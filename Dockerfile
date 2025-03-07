# Étape 1 : Build de l'application avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Définition du répertoire de travail
WORKDIR /app

# Copie des fichiers du projet (cache des dépendances Maven pour accélérer les builds)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie du reste du code source et compilation
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Création de l'image finale avec OpenJDK 21
FROM openjdk:21-jdk-slim

# Définition du répertoire de travail
WORKDIR /app

# Copie du fichier JAR généré depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposition du port de l'application
EXPOSE 8080

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
