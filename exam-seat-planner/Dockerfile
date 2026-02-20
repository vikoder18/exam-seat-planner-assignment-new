# Stage 1 — Build the jar
FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2 — Run the jar
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/exam-seat-planner-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]