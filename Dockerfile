# Build the Spring Boot fat jar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# Run the app
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system spring \
    && useradd --system --gid spring --home-dir /app --shell /usr/sbin/nologin spring

COPY --from=build /app/target/safepay-*.jar /app/app.jar
COPY data /app/data

RUN chown -R spring:spring /app

USER spring:spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
