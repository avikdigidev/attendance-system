# EventStoreSystem
**Sequence diagram**
link: https://sequencediagram.org/
title Event-Driven Architecture for Attendance System
title Attendance System Flow

entryspacing 0.9
Employee->EventStore Microservice: --send rest call to swipe in/out
EventStore Microservice --> NoSql DB : store swipe in/out data
Employee->EventStore Microservice: EOD rest call to calculate total hours
EventStore Microservice -> NoSql DB : retrieve first swipe in & last swipe out
EventStore Microservice <- NoSql DB : return first swipe in & last swipe out
EventStore Microservice -> Kafka: Publish State of Employee
note over Kafka:Kafka Topic
Attendance Microservice <- Kafka: Consume State of Employee
Attendance Microservice -> SQL DB: store state of employee
Employee->Attendance Microservice: graphql req to get total count of hours for all employee
Attendance Microservice ->SQL DB : read data
Attendance Microservice <-SQL DB : return data
Employee<-Attendance Microservice: return total count of hours for all employee
