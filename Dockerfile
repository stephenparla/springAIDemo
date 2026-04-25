# --- Stage 1: Build Stage ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime Stage ---
# 🌟 REMOVED -alpine to ensure arm64 (Mac) compatibility
FROM eclipse-temurin:17-jre
WORKDIR /app

# 🛡️ 3. Create the 'bubble' user and group for security
# Create a system user 'bubble' without a password and with a home directory
RUN useradd -m bubble
USER bubble

# 6. Expose Spring Boot port
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]