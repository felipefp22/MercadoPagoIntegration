package com.SharedCheksMercadoPagoIntegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SharedCheksMercadoPagoIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharedCheksMercadoPagoIntegrationApplication.class, args);
	}

}
