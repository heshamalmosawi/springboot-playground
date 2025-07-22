package com.springbootplayground.crudmongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CrudmongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudmongoApplication.class, args);
	}

}
