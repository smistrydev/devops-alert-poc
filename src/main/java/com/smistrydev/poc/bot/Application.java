package com.smistrydev.poc.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import de.raysha.lib.telegram.bot.api.BotAPI;

@SpringBootApplication
public class Application {

	private static final Logger L = LoggerFactory.getLogger(Application.class);
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(Application.class, args);
		L.trace("start");

		BotManger botManger = new BotManger();
		BotAPI alertBot = botManger.getBotAPI("212025635:AAGN4N3gQXq0Qv2iZvmEl5QO-2CquKFjiF8");

		BotListener botListener = new BotListener(alertBot);
		botListener.startRunning();

	}

}