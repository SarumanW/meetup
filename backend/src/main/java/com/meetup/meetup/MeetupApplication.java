package com.meetup.meetup;

import com.meetup.meetup.service.StorageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.Locale;

@SpringBootApplication
public class MeetupApplication extends SpringBootServletInitializer {

	@Resource
	StorageService storageService;

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		SpringApplication.run(MeetupApplication.class, args);
	}

	@Override
	protected WebApplicationContext run(SpringApplication application) {
		storageService.init();
		return super.run(application);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(MeetupApplication.class);
	}
}
