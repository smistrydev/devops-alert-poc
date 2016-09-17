/**
 * 
 */
package com.smistrydev.poc.update;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.model.Update;

/**
 * @author sanjaymistry
 *
 */
public interface BotUpdateHandler {

	void execute(BotAPI botAPI, Update update);

}
