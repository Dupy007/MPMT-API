# Project Management Tool (Backend)

## Description
Ce projet représente le backend de la plateforme **Project Management Tool (PMT)**. Il a été développé avec **Spring Boot** et gère les fonctionnalités principales telles que l'authentification des utilisateurs, la gestion des projets et des tâches.

## Prérequis
Assurez-vous d'avoir les outils suivants installés sur votre machine :
- **Java 21** ou une version supérieure
- **Maven 3.6+**
- **Docker** (pour la containerisation)
- **Git**

## Installation et Configuration
### 1. Cloner le dépôt
```bash
git clone https://github.com/Dupy007/MPMT-API.git
cd MPMT-API
```

### 2. Demarer le serveur mail et mysql
```bash
docker run --name mpmt-maildev -d -p 1080:1080 -p 1025:1025 maildev/maildev
docker run --name mpmt-mysql  -p 3306:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=mpmt -e MYSQL_USER=mpmt -e MYSQL_PASSWORD=mpmt  -d mysql:8.0
```

### 3. Construire et lancer l'application
```bash
mvn clean install
java -jar target/*.jar
```
### 4. Arreter le serveur mail et mysql
```bash
docker stop mpmt-mysql
docker stop mpmt-maildev
docker rm mpmt-mysql
docker rm mpmt-maildev
```

L'application sera accessible sur `http://localhost:8080`.

## Dockerisation

### 1. Construire l'image et exécuter l'application via Docker

```bash
docker-compose up --build -d
```
L'application sera accessible sur `http://localhost:8080`.