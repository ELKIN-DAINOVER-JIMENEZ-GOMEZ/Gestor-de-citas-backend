FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY Backend/pom.xml .
COPY Backend/src ./src
COPY Backend/mvnw .
COPY Backend/.mvn ./.mvn
COPY Backend/.gitignore .
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/Backend-0.0.1-SNAPSHOT.jar GestorDeCitasBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "GestorDeCitasBackend.jar"]