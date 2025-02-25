package com.yaksha.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.yaksha.assignment")
public class CreatingAndSendingGetRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreatingAndSendingGetRequestApplication.class, args);
	}
}
