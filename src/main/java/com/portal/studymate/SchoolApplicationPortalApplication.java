package com.portal.studymate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SchoolApplicationPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplicationPortalApplication.class, args);
	}

}
