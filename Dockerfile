# Dockerfile
FROM adoptopenjdk:17-jdk-hotspot

ARG JAR_FILE=target/simple-banking-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]