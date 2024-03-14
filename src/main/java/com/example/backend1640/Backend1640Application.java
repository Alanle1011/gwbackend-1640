package com.example.backend1640;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Backend1640Application {

	public static void main(String[] args) {
		SpringApplication.run(Backend1640Application.class, args);
	}

}
