package com.cxpoc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SocketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocketServiceApplication.class, args);
	}

}
