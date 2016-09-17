/**
 * 
 */
package com.smistrydev.poc.bot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.exception.BotException;
import de.raysha.lib.telegram.bot.api.model.Update;

/**
 * @author sanjaymistry
 *
 */
public class BotListener extends Thread {

	private static final Logger L = LoggerFactory.getLogger(BotListener.class);

	private static final Integer LIMIT = 20;
	private static final Integer TIMEOUT = 10;
	private BotAPI botAPI;
	private Integer offset = 0;
	private boolean running = true;
	private String status = "initialized";

	public BotListener(BotAPI botAPI) {
		this.botAPI = botAPI;
	}

	public boolean isRunning() {
		return running;
	}

	public void startRunning() {
		this.running = true;
		this.start();
	}

	public void stopRunning() {
		this.running = false;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public void run() {
		this.status = "running";
		try {
			while (running) {
				try {
					List<Update> updates = botAPI.getUpdates(offset, LIMIT, TIMEOUT);
					if (updates != null && !updates.isEmpty()) {
						for (Update update : updates) {
							offset = update.getUpdate_id() + 1;
							L.debug(update.toString());
						}
					}
				} catch (BotException e) {
					L.error(e.getMessage(), e);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			this.status = "completed";
		}
	}

}
