/**
 * 
 */
package com.smistrydev.poc.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.model.Update;

/**
 * @author sanjaymistry
 *
 */
public class ElasticUpdateHandler implements BotUpdateHandler {

	private static final Logger L = LoggerFactory.getLogger(ElasticUpdateHandler.class);

	@Override
	public void execute(BotAPI botAPI, Update update) {
		L.debug(update.toString());
	}

}
