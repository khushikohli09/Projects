package com.univ.attendograde2v;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Attendograde2vApplication {

	public static void main(String[] args) {
		SpringApplication.run(Attendograde2vApplication.class, args);
	}

}
