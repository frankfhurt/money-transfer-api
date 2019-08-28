package com.moneytransfer.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.api.clients.detail.ClientDetailResponse;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientDetailRestHelper {
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static ClientDetailResponse getDetail(int port, Long clientId) throws IOException, InterruptedException {

		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = createRequest(port, clientId);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), notNullValue());
		
		return mapper.readValue(response.body(), ClientDetailResponse.class);
	}

	private static HttpRequest createRequest(int port, Long clientId) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + port + "/clients/" + clientId))
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		return request;
	}
}