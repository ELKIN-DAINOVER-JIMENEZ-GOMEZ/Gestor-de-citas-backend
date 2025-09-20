FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiar archivos de Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Copiar pom.xml
COPY pom.xml .

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests -B

# Exponer puerto
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "target/Backend-0.0.1-SNAPSHOT.jar"]