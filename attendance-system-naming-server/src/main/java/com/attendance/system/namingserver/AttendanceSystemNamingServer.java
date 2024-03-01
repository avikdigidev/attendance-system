package com.attendance.system.namingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class AttendanceSystemNamingServer {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceSystemNamingServer.class, args);
	}

}
