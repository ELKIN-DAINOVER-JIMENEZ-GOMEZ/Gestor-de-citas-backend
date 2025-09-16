FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/Backend-0.0.1-SNAPSHOT.jar
COPY${JAR_FILE} GestorDeCitasBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "GestorDeCitasBackend.jar"]