package com.example.demo_logging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
public class DemoLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoLoggingApplication.class, args);
	}

}
