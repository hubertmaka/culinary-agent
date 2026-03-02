FROM maven:3.9.10-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre

ARG JAR_FILE="culinary-agent-1.0.0.jar"

RUN useradd -m -s /bin/bash app
WORKDIR /app
USER app
COPY --from=builder /app/target/${JAR_FILE} .

ENV SERVER_ADDRESS="0.0.0.0"
ENV SERVER_PORT="8080"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "culinary-agent-1.0.0.jar"]