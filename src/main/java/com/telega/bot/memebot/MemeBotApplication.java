package com.telega.bot.memebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class MemeBotApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(MemeBotApplication.class, args);
	}

}
