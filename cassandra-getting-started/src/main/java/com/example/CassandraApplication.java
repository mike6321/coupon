package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CassandraApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-core, application-cassandra");
		SpringApplication.run(CassandraApplication.class, args);
	}

}
