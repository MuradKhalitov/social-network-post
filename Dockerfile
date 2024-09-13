FROM openjdk:17.0.2-jdk-slim-buster
WORKDIR /app
COPY target/social-network-post-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]