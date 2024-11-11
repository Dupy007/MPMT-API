## Utiliser une image Maven officielle pour builder l'application
#FROM maven:3.8.5-openjdk-11 AS build
#
#WORKDIR /app
#
#COPY pom.xml .
#COPY src ./src
#
#RUN mvn clean package -DskipTests
#
# Utiliser une image JDK pour exécuter l'application
FROM mcr.microsoft.com/openjdk/jdk:21-distroless

ENV LANG en_US.UTF-8
ENV JAVA_HOME /usr/lib/jvm/msopenjdk-21-amd64
ENV PATH "${JAVA_HOME}/bin:${PATH}"

COPY ./target/MPMT-0.0.1.jar /MPMT.jar

EXPOSE 8080

CMD ["java", "-jar", "/MPMT.jar"]