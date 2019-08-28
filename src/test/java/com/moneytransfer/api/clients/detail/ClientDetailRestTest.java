package com.moneytransfer.api.clients.detail;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.api.ApiTest;
import com.moneytransfer.api.ClientCreateRestHelper;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientDetailRestTest extends ApiTest {
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void detail_validRequest() throws IOException, InterruptedException {
		
		Long clientId = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = createRequest(clientId);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		ClientDetailResponse responseObject = mapper.readValue(response.body(), ClientDetailResponse.class);
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), notNullValue());
		assertThat(responseObject.getId(), isA(Long.class));
		assertThat(responseObject.getName(), is("Frank"));
		assertThat(responseObject.getAccount().getBalance(), is(50.0));
	}
	
	@Test
	public void detail_invalidClientId() throws IOException, InterruptedException {
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = createRequest(12L);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(404));
		assertThat(response.body(), notNullValue());
		assertThat(response.body(), containsString("Client Not Found"));
	}
	
	private HttpRequest createRequest(Long clientId) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + PORT + "/clients/" + clientId))
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		return request;
	}

}