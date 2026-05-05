package com.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages = "com.billing")
public class BillingSoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingSoftwareApplication.class, args);
	}

}
