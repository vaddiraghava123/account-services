# Use the official Maven image to create a build artifact.
# https://hub.docker.com/_/maven

FROM openjdk:17-jdk-slim

# Step 2: Set environment variables for Mysql DB connection
ENV MYSQL_URL=jdbc:mysql://localhost:3306/mysqldemo?useSSL=false
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=Raghava123*

FROM maven:3.9.0-eclipse-temurin-17 AS build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use the official Spring Boot image for a minimal setup.
# https://hub.docker.com/_/openjdk
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build target/account-services-0.0.1-SNAPSHOT.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
