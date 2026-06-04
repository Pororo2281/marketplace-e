FROM openjdk:24-ea-21-slim-bullseye AS builder

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]