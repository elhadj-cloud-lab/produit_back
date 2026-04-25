FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

# Télécharger les dépendances Maven (optimisation du cache)
RUN --mount=type=cache,target=/root/.m2 mvn -B -q -e -DskipTests dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN addgroup --system --gid 1000 appgroup && \
    adduser --system --uid 1000 --gid 1000 appuser

COPY --from=build /app/target/*.jar app.jar

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
