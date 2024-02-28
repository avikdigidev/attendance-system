package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableFeignClients
@SpringBootApplication
public class AttendanceSystemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceSystemServiceApplication.class, args);
	}

}
