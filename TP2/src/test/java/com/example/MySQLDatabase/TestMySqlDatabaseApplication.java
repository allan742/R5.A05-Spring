package com.example.MySQLDatabase;

import org.springframework.boot.SpringApplication;

public class TestMySqlDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.from(MySqlDatabaseApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
