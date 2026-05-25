# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV DB_USERNAME=root
ENV DB_PASSWORD=root

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
