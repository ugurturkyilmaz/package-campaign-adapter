FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar","--server.address=0.0.0.0","--server.port=8081"]