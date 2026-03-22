# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Копируем pom-файлы отдельным слоем — зависимости кешируются
COPY pom.xml .
COPY rest-api/pom.xml rest-api/
COPY notifier/pom.xml notifier/

RUN mvn dependency:go-offline -pl rest-api -am -q

# Копируем исходники и собираем артефакт
COPY rest-api/src rest-api/src

RUN mvn package -pl rest-api -am -DskipTests

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /build/rest-api/target/rest-api-spring-boot.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
