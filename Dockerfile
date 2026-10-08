FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /build

COPY . .

ARG MODULE_ID

RUN mvn clean package -pl :${MODULE_ID} -am -DskipTests --quiet -Dorg.slf4j.simpleLogger.defaultLogLevel=ERROR


FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

ARG MODULE_LOCATION

COPY --from=builder /build/${MODULE_LOCATION}/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]