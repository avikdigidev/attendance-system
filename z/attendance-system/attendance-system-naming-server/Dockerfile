FROM openjdk:21
VOLUME /tmp
EXPOSE 8761
ARG JAR_FILE=target/attendance-system-naming-server-1.0.0-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]