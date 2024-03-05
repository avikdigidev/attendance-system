package com.attendance.event.store.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AttendanceEventStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceEventStoreApplication.class, args);
	}

}
