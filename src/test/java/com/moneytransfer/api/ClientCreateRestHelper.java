package com.moneytransfer.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.api.clients.create.ClientCreateResponse;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientCreateRestHelper {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static Long createClient(int port, String name, String initialDeposit) throws IOException, InterruptedException {
		String body = "{" + 
							"\"name\": \"" + name + "\"," + 
							"\"document\": \"DOC3421\"," + 
							"\"initialDeposit\" : \"" + initialDeposit + "\"" + 
						"}";

		HttpClient httpClient = HttpClient.newBuilder().build();

		HttpRequest request = createRequest(port, body);

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), notNullValue());
		
		ClientCreateResponse responseObject = mapper.readValue(response.body(), ClientCreateResponse.class);
		
		return responseObject.getId();
	}
	
	private static HttpRequest createRequest(int port, String body) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/clients"))
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(body))
				.build();
		return request;
	}
}