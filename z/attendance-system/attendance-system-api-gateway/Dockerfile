FROM openjdk:21
VOLUME /tmp
EXPOSE 8766
ARG JAR_FILE=target/attendance-system-api-gateway-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]