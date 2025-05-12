FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY out/artifacts/PFA_Back_jar/PFA_Back.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
