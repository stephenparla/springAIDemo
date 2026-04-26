# Build Stage
FROM --platform=linux/amd64 maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run Stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre
WORKDIR /app

# Security: Non-root user
RUN useradd -m bubble
USER bubble

# Performance: Optimized JVM Flags
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Copy the App
COPY --from=build /app/target/*.jar app.jar

# Standard Port for Spring Boot
EXPOSE 8080

# Execute with opts
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
