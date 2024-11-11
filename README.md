# Project Management Tool (Backend)

## Description
Ce projet représente le backend de la plateforme **Project Management Tool (PMT)**. Il a été développé avec **Spring Boot** et gère les fonctionnalités principales telles que l'authentification des utilisateurs, la gestion des projets et des tâches.

## Prérequis
Assurez-vous d'avoir les outils suivants installés sur votre machine :
- **Java 21** ou une version supérieure
- **Maven 3.6+**
- **Docker** (pour la containerisation)
- **PostgreSQL** (ou MySQL, selon votre configuration)
- **Git**

## Installation et Configuration
### 0. demarer le serveur web et mysql
```bash
docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev
docker run --name mpmt-mysql  -p 33061:3306 -e MYSQL_ALLOW_EMPTY_PASSWORD=true -d mysql
```

### 1. Cloner le dépôt
```bash
git clone https://github.com/Dupy007/MPMT-API.git
cd MPMT-API
```

### 2. Configurer la base de données
Assurez-vous que votre base de données PostgreSQL ou MySQL est en cours d'exécution.
```bash
mysql -u$YOURUSERNAMEMYSQL -p < ./src/main/sql/db.sql
```
Modifiez le fichier `src/main/resources/application.properties` ou `application.yml` pour y ajouter vos configurations de base de données :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/MPMT
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```
### 3. Construire et lancer l'application
```bash
mvn clean install
mvn spring-boot:run &
```

L'application sera accessible sur `http://localhost:8080`.

[//]: # (## Dockerisation)

[//]: # (### 1. Construire l'image Docker)

[//]: # (```bash)

[//]: # (docker build -t mpmt-api .)

[//]: # (```)

[//]: # ()
[//]: # (### 2. Exécuter l'application via Docker)

[//]: # (```bash)

[//]: # (docker run -d -p 8080:8080 mpmt-api)

[//]: # (```)
