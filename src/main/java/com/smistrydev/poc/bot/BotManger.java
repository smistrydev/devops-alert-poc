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

}
