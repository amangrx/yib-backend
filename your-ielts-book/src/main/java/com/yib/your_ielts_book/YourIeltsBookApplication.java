package com.yib.your_ielts_book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class YourIeltsBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(YourIeltsBookApplication.class, args);
	}

}
