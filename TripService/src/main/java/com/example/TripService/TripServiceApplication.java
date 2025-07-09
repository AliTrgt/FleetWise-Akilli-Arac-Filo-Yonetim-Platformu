package com.example.TripService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableAsync
public class TripServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripServiceApplication.class, args);
	}

}
