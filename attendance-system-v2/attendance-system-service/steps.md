Start Kafka

PATH : C:\Users\darsaraf\kafka\bin\windows

zookeeper-server-start.bat config\zookeeper.properties
kafka-server-start.bat config\server.properties
java -Dmicronaut.config.files=application.yml -jar akhq-0.24.0-all.jar

Start Cassandra

C:\Users\darsaraf\cassandra3.11.9\bin>cassandra

CREATE TABLE attendance_sys.events
(employeeid bigint ,eventtimestamp timestamp,eventtype text,
PRIMARY KEY (employeeid, eventtimestamp)
);
