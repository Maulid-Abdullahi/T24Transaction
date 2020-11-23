package com.example.T24Transactions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class T24TransactionsApplication {

	public static void main(String[] args) {
		new T24TestClient("127.0.0.1", 55555).run();
		SpringApplication.run(T24TransactionsApplication.class, args);
	}

}
