/**
 * 
 */
package com.smistrydev.poc.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.exception.BotException;
import de.raysha.lib.telegram.bot.api.model.ChatId;
import de.raysha.lib.telegram.bot.api.model.Update;

/**
 * @author sanjaymistry
 *
 */
public class BasicUpdateHandler implements BotUpdateHandler {

	private static final Logger L = LoggerFactory.getLogger(BasicUpdateHandler.class);

	@Override
	public void execute(BotAPI botAPI, Update update) {
		L.debug(update.toString());

		ChatId chatId = new ChatId(update.getMessage().getChat().getId());
		try {
			botAPI.sendMessage(chatId, "Echo: " + update.getMessage().getText());
		} catch (BotException e) {
			throw new RuntimeException(e);
		}

	}

}
