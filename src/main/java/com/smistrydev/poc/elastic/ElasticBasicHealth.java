package com.smistrydev.poc.elastic;

import java.io.IOException;

import org.json.JSONObject;

import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;

public class ElasticBasicHealth {

	public ElasticBasicHealth() {
	}

	public String getHealth() {

		String result = "black";

		JestClientFactory factory = new JestClientFactory();
		// factory.setHttpClientConfig(new HttpClientConfig.Builder("https://5f658568e210b218e632034026dd0b29.ap-southeast-2.aws.found.io:9243/_cluster/health").defaultCredentials("admin",
		// "Hello#123").build());
		factory.setHttpClientConfig(new HttpClientConfig.Builder("http://5f658568e210b218e632034026dd0b29.ap-southeast-2.aws.found.io:9200/").defaultCredentials("admin", "Hello#123").build());

		JestClient client = factory.getObject();

		try {
			JestResult jestResult = client.execute(new Health.Builder().build());
			JsonObject jsonObject = jestResult.getJsonObject();
			result = jsonObject.get("status").getAsString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.shutdownClient();
		}

		return result;
	}

	public static void main(String[] args) {
		ElasticBasicHealth elasticBasicHealth = new ElasticBasicHealth();
		System.out.println("Cluster Health is: " + elasticBasicHealth.getHealth());
	}

}
