**Sequence diagram**
link: https://sequencediagram.org/
title Event-Driven Architecture for Attendance System

participant "Employee" as Emp
participant "Event Store Microservice" as ES
participant "Kafka" as K
participant "Attendance Microservice" as AS
participant "Attendance Database" as DB

note over Emp: Employee swipes in/out

Emp->ES: Swipe In
ES->K: Publish Swipe In Event
K->AS: Consume Swipe In Event
note over K: Kafka topics:\n- swipe-in-topic\n- swipe-out-topic
AS->DB: Update Attendance Record

Emp->ES: Swipe Out
ES->K: Publish Swipe Out Event
K->AS: Consume Swipe Out Event
AS->DB: Update Attendance Record

note over AS: Perform End-of-Day Calculation
AS->DB: Calculate Total Hours for Employees




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

#mysql commands
CREATE DATABASE attendance_db;
\
CREATE TABLE `attendance_record` (
  `id` varchar(255) NOT NULL,
  `date` date DEFAULT NULL,
  `employee_id` varchar(255) NOT NULL,
  `swipe_in_timestamp` datetime(6) NOT NULL,
  `swipe_out_timestamp` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
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
(UUID(),'2024-02-24', '1', '2024-02-24 09:00:00', '2024-02-24 17:00:00'),
(UUID(),'2024-02-24', '2', '2024-02-24 08:30:00', '2024-02-24 16:30:00'),
(UUID(),'2024-02-24', '3', '2024-02-24 09:15:00', '2024-02-24 17:15:00'),
(UUID(),'2024-02-24', '4', '2024-02-24 09:30:00', '2024-02-24 17:30:00'),
(UUID(),'2024-02-24', '5', '2024-02-24 08:45:00', '2024-02-24 16:45:00'),
(UUID(),'2024-02-24', '6', '2024-02-24 08:00:00', '2024-02-24 16:00:00'),
(UUID(),'2024-02-24', '7', '2024-02-24 09:45:00', '2024-02-24 17:45:00'),
(UUID(),'2024-02-24', '8', '2024-02-24 10:00:00', '2024-02-24 18:00:00'),
(UUID(),'2024-02-24', '9', '2024-02-24 10:15:00', '2024-02-24 18:15:00'),
(UUID(),'2024-02-24', '10', '2024-02-24 10:30:00', '2024-02-24 18:30:00');




#kafka
docker pull confluentinc/cp-kafka
\
docker run -d --name kafka -p 9092:9092 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 confluentinc/cp-kafka
\
docker exec -it kafka kafka-topics --create --topic swipe-in-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
\
docker exec -it kafka kafka-topics --create --topic swipe-out-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
\
docker exec -it kafka kafka-topics --create --topic eod-total-hours-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1

