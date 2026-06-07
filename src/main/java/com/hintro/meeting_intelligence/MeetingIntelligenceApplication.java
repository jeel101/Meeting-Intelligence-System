package com.hintro.meeting_intelligence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeetingIntelligenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetingIntelligenceApplication.class, args);
	}

}
