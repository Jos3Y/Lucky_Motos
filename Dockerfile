# Usar imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Instalar Maven
RUN apk add --no-cache maven

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar el proyecto
RUN mvn clean install -DskipTests

# Exponer puerto
EXPOSE 8081

# Comando para iniciar la aplicación
CMD ["java", "-jar", "target/sistemamotoservice-0.0.1-SNAPSHOT.war"]

