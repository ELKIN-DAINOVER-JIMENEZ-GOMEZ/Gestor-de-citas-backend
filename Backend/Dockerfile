FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/Backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} GestorDeCitasBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "GestorDeCitasBackend.jar"]