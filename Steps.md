**Sequence diagram**
link: https://sequencediagram.org/
title Event-Driven Attendance System Flow

participant "(MS)Attendance REST API" as API
participant "(MS)Event Service" as ES
participant "Kafka" as K
participant "Cassandra" as Cass
participant "(MS)Attendance Calculation Service" as ACS
participant "(MS)Attendance Read Store" as ARS
participant "MySQL" as SQL

note over ACS: Consumer for Kafka topic: Swipe Event\nPublisher for Kafka topic: EOD Calculation

note over ARS: Consumer for Kafka topic: EOD Calculation

note over API: Employee swipes in/out
API->ES: POST /events/swipe
ES->K: Publish Swipe Event
note over K:Kafka topic: Swipe Event
ACS->K: Consume Swipe Event
ACS->Cass:Write consumed swipe events
Cass->ACS:Query swipe events
ACS->ACS: Perform EOD calculation
ACS->K: Publish EOD Calculation Event
note over K: Kafka topic: EOD Calculation
ARS->K: Consume EOD Calculation Event
ARS->SQL: Persist attendance records
API->SQL: Retrieve attendance records


#ports
api gateway - localhost =8761
eureka - localhost =8765
attendance-service = 8987
attendance-service-gql = 9190
cassandra =8100
mysql = 3307
kafka =9099
zookeeper= 2881
config-server= 8888

**setting up the db:**
#mysql
docker pull mysql:latest
\
docker run --name attendance-system -e MYSQL_ROOT_PASSWORD=8c2xC3597Rs -p 3307:3306 mysql
\
docker ps
\
docker exec -it attendance-system bash
\
docker port attendance-system 
\
mysql -u root -p

\
need to start the container again?\
docker start attendance-system
#mysql commands
CREATE DATABASE attendance_db;
\
CREATE TABLE `attendance_record` (
  `date` date DEFAULT NULL,
  `employee_id` varchar(255) NOT NULL,
  `swipe_in_timestamp` datetime(6) NOT NULL,
  `swipe_out_timestamp` datetime(6) NOT NULL,
  PRIMARY KEY (`employee_id`)
) ;
\
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `department` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO attendance_db.employee (department, name) VALUES
('IT', 'John Doe'),
('HR', 'Jane Smith'),
('Finance', 'Michael Johnson'),
('Marketing', 'Emily Davis'),
('Operations', 'David Wilson'),
('Sales', 'Sarah Brown'),
('Research and Development', 'Christopher Martinez'),
('Customer Service', 'Amanda Taylor'),
('Legal', 'James Anderson'),
('Administration', 'Jennifer Thomas');
INSERT INTO attendance_db.attendance_record (id, date, employee_id, swipe_in_timestamp, swipe_out_timestamp) VALUES
(UUID(),'2024-02-23', '1', '2024-02-23 09:00:00', '2024-02-23 17:00:00'),
(UUID(),'2024-02-23', '2', '2024-02-23 08:30:00', '2024-02-23 16:30:00'),
(UUID(),'2024-02-23', '3', '2024-02-23 09:15:00', '2024-02-23 17:15:00'),
(UUID(),'2024-02-23', '4', '2024-02-23 09:30:00', '2024-02-23 17:30:00'),
(UUID(),'2024-02-23', '5', '2024-02-23 08:45:00', '2024-02-23 16:45:00'),
(UUID(),'2024-02-23', '6', '2024-02-23 08:00:00', '2024-02-23 16:00:00'),
(UUID(),'2024-02-23', '7', '2024-02-23 09:45:00', '2024-02-23 17:45:00'),
(UUID(),'2024-02-23', '8', '2024-02-23 10:00:00', '2024-02-23 18:00:00'),
(UUID(),'2024-02-23', '9', '2024-02-23 10:15:00', '2024-02-23 18:15:00'),
(UUID(),'2024-02-23', '10', '2024-02-23 10:30:00', '2024-02-23 18:30:00');


#cassandra
docker run --name attendance_cassandra -d -e CASSANDRA_USER=username -e CASSANDRA_PASSWORD=8105xj379jss -p 8100:9042 cassandra:latest

docker start attendance_cassandra
docker start kafka

--script
CREATE KEYSPACE attendance_sys WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

CREATE TABLE event_details (
    employeeid INT,
    eventtimestamp TIMESTAMP,
    eventtype TEXT,
    PRIMARY KEY (employeeid, eventtimestamp)
);

#kafka

\
docker run -d --name kafka -p 9099:9092 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2881 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9099 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 wurstmeister/kafka
\
docker exec -it kafka kafka-topics --create --topic swipe-in-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
\
docker exec -it kafka kafka-topics --create --topic swipe-out-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
\
docker exec -it kafka kafka-topics --create --topic eod-total-hours-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

docker run -d --name zookeeper -p 2881:2181 wurstmeister/zookeeper

#kafka ui
docker run -d --name kafka-ui -it -p 8080:8080 -e DYNAMIC_CONFIG_ENABLED=true provectuslabs/kafka-ui

docker run -d --name kafka -p 9099:9092   -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2881   -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9099   -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1   wurstmeister/kafka
