FROM eclipse-temurin:11-jdk-alpine

WORKDIR /app

COPY target/weather-forecast-api-1.0.0.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
