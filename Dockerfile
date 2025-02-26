FROM openjdk:22-ea-21-jdk-bookworm
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/app.jar"]
