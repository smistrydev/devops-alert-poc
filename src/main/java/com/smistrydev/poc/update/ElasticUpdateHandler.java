/**
 * 
 */
package com.smistrydev.poc.update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

import de.raysha.lib.telegram.bot.api.BotAPI;
import de.raysha.lib.telegram.bot.api.BotAPI.ParseMode;
import de.raysha.lib.telegram.bot.api.exception.BotException;
import de.raysha.lib.telegram.bot.api.model.ChatId;
import de.raysha.lib.telegram.bot.api.model.ReplyKeyboardHide;
import de.raysha.lib.telegram.bot.api.model.ReplyKeyboardMarkup;
import de.raysha.lib.telegram.bot.api.model.Update;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;

/**
 * @author sanjaymistry
 *
 */
public class ElasticUpdateHandler implements BotUpdateHandler {

	private static final String ES_URL = "http://5f658568e210b218e632034026dd0b29.ap-southeast-2.aws.found.io:9200/";
	private static final String ES_USER = "admin";
	private static final String ES_PASS = "Hello#123";

	private static final Logger L = LoggerFactory.getLogger(ElasticUpdateHandler.class);
	private JestClientFactory factory;

	public ElasticUpdateHandler() {
		factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(ES_URL).defaultCredentials(ES_USER, ES_PASS).build());
	}

	public static void main(String[] args) {
		ElasticUpdateHandler elasticUpdateHandler = new ElasticUpdateHandler();
		// System.out.println(elasticUpdateHandler.getHealth());
		System.out.println(elasticUpdateHandler.getIndices());

	}

	@Override
	public void execute(BotAPI botAPI, Update update) {
		L.debug(update.toString());

		if (update.getMessage() == null || update.getMessage().getText() == null) {
			return;
		}

		String messageText = update.getMessage().getText();

		L.debug(" ------ Message: " + messageText);

		if (messageText.startsWith("/start")) {
			ChatId chatId = new ChatId(update.getMessage().getChat().getId());
			try {
				ReplyKeyboardHide replyKeyboardHide = new ReplyKeyboardHide();
				replyKeyboardHide.setHide_keyboard(true);
				botAPI.sendMessage(chatId, "Welcome to Elastic Alert Bot", null, false, null, replyKeyboardHide);
			} catch (BotException e) {
				throw new RuntimeException(e);
			}
		}

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

		if (messageText.startsWith("/dashboard")) {

			ChatId chatId = new ChatId(update.getMessage().getChat().getId());
			try {
				ParseMode parseMode = ParseMode.Markdown;
				Boolean disableWebPagePreview = true;
				Integer replyToMessageId = null;

				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

				List<String> dashboardListMarvel = new ArrayList<>();
				dashboardListMarvel.add("Dashboard: Marvel");
				List<String> dashboardListTwitter = new ArrayList<>();
				dashboardListTwitter.add("Dashboard: Twitter");
				List<String> dashboardListGraph = new ArrayList<>();
				dashboardListGraph.add("Dashboard: Graph");

				List<List<String>> keyboard = new ArrayList<>();
				keyboard.add(dashboardListMarvel);
				keyboard.add(dashboardListTwitter);
				keyboard.add(dashboardListGraph);

				replyKeyboardMarkup.setKeyboard(keyboard);
				replyKeyboardMarkup.setOne_time_keyboard(true);
				replyKeyboardMarkup.setResize_keyboard(true);
				Object replyMarkup = replyKeyboardMarkup;
				botAPI.sendMessage(chatId, "Please choose the dashboard", parseMode, disableWebPagePreview, replyToMessageId, replyMarkup);

				// botAPI.sendDocument(chatId, new File("/Users/sanjaymistry/java/docs/Tweeter.png"));
			} catch (BotException e) {
				throw new RuntimeException(e);
			}
		}

		if (messageText.startsWith("Dashboard:")) {

			ChatId chatId = new ChatId(update.getMessage().getChat().getId());
			try {
				ReplyKeyboardHide replyKeyboardHide = new ReplyKeyboardHide();
				replyKeyboardHide.setHide_keyboard(true);

				if (messageText.endsWith("Marvel")) {
					botAPI.sendDocument(chatId, new File("/Users/sanjaymistry/java/docs/Marvel.png"), null, replyKeyboardHide);
				}
				if (messageText.endsWith("Twitter")) {
					botAPI.sendDocument(chatId, new File("/Users/sanjaymistry/java/docs/Tweeter.png"), null, replyKeyboardHide);
				}
				if (messageText.endsWith("Graph")) {
					botAPI.sendDocument(chatId, new File("/Users/sanjaymistry/java/docs/Graph.png"), null, replyKeyboardHide);
				}
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
		String result = "";
		try {
			GetRequest request = Unirest.get("http://5f658568e210b218e632034026dd0b29.ap-southeast-2.aws.found.io:9200/_stats?pretty=true").basicAuth(ES_USER, ES_PASS);
			HttpResponse<JsonNode> response = request.asJson();
			JSONObject indices = response.getBody().getObject().getJSONObject("indices");
			@SuppressWarnings("unchecked")
			Set<String> keys = indices.keySet();
			for (String key : keys) {
				if (key.startsWith(".kibana") || key.startsWith(".marvel")) {
					continue;
				}
				Integer count = indices.getJSONObject(key).getJSONObject("total").getJSONObject("docs").getInt("count");
				result = result + "index:" + key + " count: " + count + "\n";
			}

		} catch (Exception e) {
			result = "unknown";
		}
		if (result.isEmpty()) {
			result = "No Indices found.";
		}

		return result;
	}

	public void shutClient() {
		// jestClient.shutdownClient();
	}

}
