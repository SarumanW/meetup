package com.meetup.meetup;

import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.service.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Locale;

@SpringBootApplication
@EnableScheduling
public class MeetupApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		SpringApplication.run(MeetupApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MeetupApplication.class);
	}
}
