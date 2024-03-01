FROM openjdk:21
VOLUME /tmp
EXPOSE 8505
ARG JAR_FILE=target/attendance-consumer-service-1.0.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]