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

