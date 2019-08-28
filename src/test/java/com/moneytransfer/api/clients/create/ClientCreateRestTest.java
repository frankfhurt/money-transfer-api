package com.moneytransfer.api.clients.create;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.Mockito;

import com.moneytransfer.api.ApiTest;
import com.moneytransfer.api.BusinessService;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientCreateRestTest extends ApiTest {

	@Test
	public void create_validRequest() throws IOException, InterruptedException {
		
		String body = "{" + 
							"\"name\": \"Franklyn\"," + 
							"\"document\": \"DOC4321\"," + 
							"\"initialDeposit\" : 10.00" + 
						"}";

		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = buildRequest(body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), notNullValue());
		assertThat(response.body(), containsString("id"));
		assertThat(response.body(), containsString("name"));
		assertThat(response.body(), containsString("account"));
		assertThat(response.body(), containsString("balance"));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void create_whenCallBusiness_ExceptionThrown() {

		@SuppressWarnings("rawtypes")
		BusinessService business = Mockito.mock(BusinessService.class);
		ClientCreateRest rest = new ClientCreateRest(business);
		when(business.execute(any())).thenThrow(new RuntimeException());

		Response response = rest.create(new ClientCreateRequest());
		
		assertThat(response.getStatus(), is(500));
	}
	
	@Test
	public void create_requestWithoutName() throws IOException, InterruptedException {
		
		String body = "{" + 
				"\"document\": \"DOC4321\"," + 
				"\"initialDeposit\" : 10.00" + 
				"}";
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = buildRequest(body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), notNullValue());
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("Field name is required"));
	}
	
	@Test
	public void create_requestWithoutDocument() throws IOException, InterruptedException {
		
		String body = "{" + 
				"\"name\": \"Franklyn\"," + 
				"\"initialDeposit\" : 10.00" + 
				"}";
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = buildRequest(body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), notNullValue());
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("Field document is required"));
	}
	
	@Test
	public void create_requestWithoutInitialDeposit() throws IOException, InterruptedException {
		
		String body = "{" + 
				"\"name\": \"Franklyn\"," + 
				"\"document\": \"DOC4321\"" + 
				"}";
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = buildRequest(body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), notNullValue());
		assertThat(response.body(), containsString("id"));
		assertThat(response.body(), containsString("name"));
		assertThat(response.body(), containsString("account"));
		assertThat(response.body(), containsString("\"balance\":0"));
	}
	
	@Test
	public void create_emptyRequest() throws IOException, InterruptedException {
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = buildRequest("");
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), notNullValue());
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("request cannot be null"));
	}

	private HttpRequest buildRequest(String body) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + PORT + "/clients/"))
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(body))
				.build();
		return request;
	}

}