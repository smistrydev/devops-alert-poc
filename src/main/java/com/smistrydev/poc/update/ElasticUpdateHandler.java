/**
 * 
 */
package com.smistrydev.poc.update;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.exception.BotException;
import de.raysha.lib.telegram.bot.api.model.ChatId;
import de.raysha.lib.telegram.bot.api.model.Update;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;
import io.searchbox.core.Cat;
import io.searchbox.core.CatResult;
import io.searchbox.core.Get;

/**
 * @author sanjaymistry
 *
 */
public class ElasticUpdateHandler implements BotUpdateHandler {

	private static final Logger L = LoggerFactory.getLogger(ElasticUpdateHandler.class);
	private JestClientFactory factory;

	public ElasticUpdateHandler() {
		factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder("http://5f658568e210b218e632034026dd0b29.ap-southeast-2.aws.found.io:9200/").defaultCredentials("admin", "Hello#123").build());
	}

	public static void main(String[] args) {
		ElasticUpdateHandler elasticUpdateHandler = new ElasticUpdateHandler();
//		System.out.println(elasticUpdateHandler.getHealth());
		System.out.println(elasticUpdateHandler.getIndices());
		
		
	}
	
	@Override
	public void execute(BotAPI botAPI, Update update) {
		L.debug(update.toString());

		String messageText = update.getMessage().getText();

		if (messageText.startsWith("/health")) {
			ChatId chatId = new ChatId(update.getMessage().getChat().getId());
			try {
				botAPI.sendMessage(chatId, this.getHealth());
			} catch (BotException e) {
				throw new RuntimeException(e);
			}
		}

		if (messageText.startsWith("/count")) {
			ChatId chatId = new ChatId(update.getMessage().getChat().getId());
			try {
				botAPI.sendMessage(chatId, this.getIndices());
			} catch (BotException e) {
				throw new RuntimeException(e);
			}
		}

	}

	public String getHealth() {
		String result = "Health of Cluster: unknown";
		JestClient jestClient = factory.getObject();
		try {
			JestResult jestResult = jestClient.execute(new Health.Builder().build());
			JsonObject jsonObject = jestResult.getJsonObject();
			result = "Health of Cluster: " + jsonObject.get("status").getAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			jestClient.shutdownClient();
		}

		return result;
	}

	public String getIndices() {
		String result = "unknown";
		JestClient jestClient = factory.getObject();
		try {
			JestResult jestResult = jestClient.execute(new Get.Builder(null, null).build());
			//JsonObject jsonObject = catResult.getJsonObject();
			//result = jsonObject.get("status").getAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			jestClient.shutdownClient();
		}

		return result;
	}

	public void shutClient() {
		// jestClient.shutdownClient();
	}

}
