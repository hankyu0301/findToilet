FROM  openjdk:17-jdk

ARG JAR_FILE=/build/libs/findToilet-0.0.0.jar

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "/app.jar"]