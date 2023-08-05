# Dockerfile
FROM maven:3.8.5-openjdk-17 AS build

COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package

FROM amazoncorretto:17-alpine

COPY --from=build /app/target/simple-banking-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
