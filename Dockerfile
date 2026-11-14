FROM docker.io/library/maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -B dependency:go-offline

COPY src ./src
RUN mvn -q -B package -DskipTests

FROM docker.io/library/eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS=""
ENV JAVA_ARGS="-DCONFIG_SERVER_URL=http://192.168.1.135:8888 -DEUREKA_URI=http://192.168.1.135:8761"

USER app
EXPOSE 8085

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS $JAVA_ARGS -jar app.jar"]
