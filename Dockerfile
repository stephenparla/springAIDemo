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
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 🛡️ 3. Create the 'bubble' user and group for security
RUN addgroup -S bubble && adduser -S bubble -G bubble

# 4. Copy the jar and change ownership to the 'bubble' user
# This ensures the 'bubble' user has permission to read the file
COPY --from=build --chown=bubble:bubble /app/target/*.jar app.jar

# 👤 5. Switch to the 'bubble' user
USER bubble

# 6. Expose Spring Boot port
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]