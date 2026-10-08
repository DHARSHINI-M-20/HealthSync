FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package dependency:copy-dependencies -DoutputDirectory=target/dependency

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/HealthSync.jar ./HealthSync.jar
COPY --from=build /app/target/dependency ./dependency
ENV PORT=10000
EXPOSE 10000
CMD ["sh", "-c", "java -cp 'HealthSync.jar:dependency/*' com.healthsync.web.WebServer"]
