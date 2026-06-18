# Etapa 1: Build (Maven + JDK 17)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cacheia dependências
COPY pom.xml .
RUN mvn -q -e dependency:go-offline

# Compila o jar (sem rodar testes)
COPY src ./src
RUN mvn -q clean package -DskipTests

# Etapa 2: Runtime (JRE 17 enxuto)
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/to-do-list-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
