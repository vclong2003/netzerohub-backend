FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /build

COPY . .

ARG MODULE

RUN mvn clean package -pl :${MODULE} -am -DskipTests


FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

ARG MODULE

COPY --from=builder /build/${MODULE}/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]