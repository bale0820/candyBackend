FROM gradle:8.2.1-jdk17 AS build
WORKDIR /home/gradle/project
COPY . .
RUN gradle build -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
