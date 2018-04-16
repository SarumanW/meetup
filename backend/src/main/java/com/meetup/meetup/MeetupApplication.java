package com.meetup.meetup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.Locale;

@SpringBootApplication
//@ImportResource("classpath:applicationContext.xml")
public class MeetupApplication {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		SpringApplication.run(MeetupApplication.class, args);
	}
}
