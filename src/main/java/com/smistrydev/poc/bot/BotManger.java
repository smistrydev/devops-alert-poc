/**
 * 
 */
package com.smistrydev.poc.bot;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.TelegramBot;

/**
 * @author sanjaymistry
 *
 */
public class BotManger {

	public BotManger() {
	}

	public BotAPI getBotAPI(String token) {
		BotAPI botAPI = new TelegramBot(token);
		return botAPI;
	}

	public static void main(String[] args) {
		System.out.println("Start");

		BotManger botManger = new BotManger();
		BotAPI alertBot = botManger.getBotAPI("212025635:AAGN4N3gQXq0Qv2iZvmEl5QO-2CquKFjiF8");
		
		BotListener botListener = new BotListener(alertBot);
		botListener.startRunning();

		System.out.println("Stop");
	}

}
