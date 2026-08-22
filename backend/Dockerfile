# ---- Stage 1: Build the application with Maven ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only the pom first so dependency downloads are cached across builds
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Now copy the rest of the source and build
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---- Stage 2: Run the built jar on a lightweight JRE image ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/graphapp-1.0.0.jar app.jar

# Render (and most platforms) inject PORT; the app already reads it via
# ${PORT:8080} in application.yml, so no hardcoded port here.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
