package com.attendance.consumer.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AttendanceConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceConsumerServiceApplication.class, args);
	}

}
