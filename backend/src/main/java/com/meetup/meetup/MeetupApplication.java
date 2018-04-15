package com.meetup.meetup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class MeetupApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetupApplication.class, args);
	}
}
