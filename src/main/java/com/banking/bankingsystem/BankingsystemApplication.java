package com.banking.bankingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync //Enables @Async for background email
public class BankingsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingsystemApplication.class, args);
	}

}
