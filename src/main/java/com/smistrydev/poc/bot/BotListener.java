/**
 * 
 */
package com.smistrydev.poc.bot;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smistrydev.poc.update.BotUpdateHandler;

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
	private BotUpdateHandler handler;

	private HashMap<String, String> botMap = new HashMap<>();

	public BotListener(BotAPI botAPI) {
		this.botAPI = botAPI;

		// Maps of all API to Handler Class name
		this.botMap.put("POC_Basic_Bot", "com.smistrydev.poc.update.BasicUpdateHandler");
		this.botMap.put("POC_Alert_Bot", "com.smistrydev.poc.update.ElasticUpdateHandler");

		try {
			@SuppressWarnings("unchecked")
			Class<BotUpdateHandler> botHandlerClass = (Class<BotUpdateHandler>) Class.forName(this.botMap.get(this.botAPI.getMe().getUsername()));
			this.handler = botHandlerClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

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
							this.handler.execute(botAPI, update);
						}
					}
				} catch (BotException e) {
					// L.error(e.getMessage(), e);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			this.status = "completed";
		}
	}

}
