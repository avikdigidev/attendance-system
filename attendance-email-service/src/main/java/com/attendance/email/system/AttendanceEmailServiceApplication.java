package com.attendance.email.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class AttendanceEmailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceEmailServiceApplication.class, args);
	}


}
