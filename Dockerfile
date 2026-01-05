# BUILD

FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

# RUNTIME

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S quarkus && adduser -S -G quarkus quarkus

WORKDIR /app

COPY --from=build /app/target/quarkus-app .

RUN chown -R quarkus:quarkus /app

USER quarkus

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
