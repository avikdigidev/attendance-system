package com.avikdigidev.attendance.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.avikdigidev.attendance.constants.AttendanceConstants.SWIPE_IN_TOPIC;

@Configuration
public class KafkaConfig {


    private final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(SWIPE_IN_TOPIC).build();
    }


   // @KafkaListener(topics = SWIPE_IN_TOPIC, groupId = ATTENDANCE_GROUP_ID)
    public void listenSwipeInEvent(String message) {
        logger.info(message);
        // Logic to handle swipe in event
    }

  //  @KafkaListener(topics = SWIPE_OUT_TOPIC, groupId = ATTENDANCE_GROUP_ID)
    public void listenSwipeOutEvent(String message) {
        logger.info(message);
    }


}

