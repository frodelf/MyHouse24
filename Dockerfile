FROM openjdk:17.0.2-jdk-slim-buster
COPY target/*.jar MyHouse24.jar
ENTRYPOINT ["java", "-jar", "MyHouse24.jar"]