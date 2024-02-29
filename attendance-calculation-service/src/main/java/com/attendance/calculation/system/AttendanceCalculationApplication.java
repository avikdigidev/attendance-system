package com.attendance.calculation.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AttendanceCalculationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceCalculationApplication.class, args);
	}

}
