# ---- Build Stage ----
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jre-alpine AS runner
LABEL org.opencontainers.image.title="Bank Account Management" \
      org.opencontainers.image.description="A Spring-boot-based application for managing bank accounts." \
      org.opencontainers.image.version="1.0.0" \
      org.opencontainers.image.authors="Jefferson Condotta <jefferson.condotta@gmail.com>" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.url="https://github.com/jcondotta/bank-account-management"

WORKDIR /app

RUN apk add --no-cache curl

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=builder /app/target/bank-account-management-*.jar ./app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
   CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]