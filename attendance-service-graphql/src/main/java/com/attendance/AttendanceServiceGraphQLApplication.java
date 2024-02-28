package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AttendanceServiceGraphQLApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceServiceGraphQLApplication.class, args);
	}

}
