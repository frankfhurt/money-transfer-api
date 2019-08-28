package com.moneytransfer.api.clients.accounts.transactions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.moneytransfer.api.ApiTest;
import com.moneytransfer.api.ClientCreateRestHelper;
import com.moneytransfer.api.ClientDetailRestHelper;
import com.moneytransfer.api.clients.detail.ClientDetailResponse;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class TransferAmountRestTest extends ApiTest {

	@Test
	public void transfer_validRequest_shouldTransferAmounts() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 5," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";

		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), containsString("newBalance"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(45.0));
		assertThat(toDetail.getAccount().getBalance(), is(5.0));
	}
	
	@Test
	public void transfer_amountWithTwoDecimals_shouldTransferAmounts() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 5.15," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(200));
		assertThat(response.body(), containsString("newBalance"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(44.85));
		assertThat(toDetail.getAccount().getBalance(), is(5.15));
	}
	
	@Test
	public void transfer_amountWithMoreThanTwoDecimals_invalidRequestError() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 5.123," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("amount must be a valid positive number. Max of two decimals"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(50.0));
		assertThat(toDetail.getAccount().getBalance(), is(0.0));
	}
	
	@Test
	public void transfer_noAmount_InvalidRequestError() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("amount cannot be null"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(50.0));
		assertThat(toDetail.getAccount().getBalance(), is(0.0));
	}

	@Test
	public void transfer_invalidToClientId() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 5," + 
							"\"toClientId\" : \"value\"" +
						"}";

		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("toClientId must be a valid positive number"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(50.0));
		assertThat(toDetail.getAccount().getBalance(), is(0.0));
	}
	
	@Test
	public void transfer_noToClientId_InvalidRequestError() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 5" + 
						"}";
		
		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("toClientId cannot be null"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(50.0));
		assertThat(toDetail.getAccount().getBalance(), is(0.0));
	}
	
	@Test
	public void transfer_emptyRequest_InvalidRequestError() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "";
		
		HttpResponse<String> response = sendRequest(from, body);
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("request cannot be null"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(50.0));
		assertThat(toDetail.getAccount().getBalance(), is(0.0));
	}
	
	@Test
	public void transfer_invalidClientId_InvalidRequestError() throws IOException, InterruptedException {
		
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 5," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = createRequest(12L, body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("clientId"));
		assertThat(response.body(), containsString("Client Not Found"));
		
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(toDetail.getAccount().getBalance(), is(0.0));
	}

	@Test
	public void transfer_invalidtoClientId_InvalidRequestError() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "50");
		
		String body = "{\r\n" + 
							"\"amount\" : 5," + 
							"\"toClientId\" : \"12\"" + 
						"}";

		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = createRequest(from, body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		
		assertThat(response.statusCode(), is(400));
		assertThat(response.body(), containsString("Invalid request"));
		assertThat(response.body(), containsString("toClientId"));
		assertThat(response.body(), containsString("Client Not Found"));
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		
		assertThat(fromDetail.getAccount().getBalance(), is(50.0));
	}
	
	@Test
	public void transfer_validRequestConcurrent() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "500");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 1," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		int numberOfTransfers = 500;
		
		CountDownLatch countDown = new CountDownLatch(numberOfTransfers);
		
		Runnable transferAmountTask = () -> {
			HttpClient httpClient = HttpClient.newBuilder().build();
			
			HttpRequest request = createRequest(from, body);
			
			HttpResponse<String> response;
			try {
				response = httpClient.send(request, BodyHandlers.ofString());
				assertThat(response.statusCode(), is(200));
				assertThat(response.body(), containsString("newBalance"));
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} finally {
				countDown.countDown();
			}
		};
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		for (int i = 0; i < numberOfTransfers; i++) {
			executor.execute(transferAmountTask);
		}
		
		countDown.await();
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(0.0));
		assertThat(toDetail.getAccount().getBalance(), is(500.0));
	}
	
	@Test
	public void transfer_validRequestConcurrent50Threads200RequestsPerThread() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "1000");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 0.01," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		int threads = 50;
		
		CountDownLatch countDown = new CountDownLatch(threads);
		
		Runnable transferAmountTask = () -> {
			HttpClient httpClient = HttpClient.newBuilder().build();
			
			HttpRequest request = createRequest(from, body);
			
			HttpResponse<String> response;
			try {
				
				for (int i = 0; i < 200; i++) {
					response = httpClient.send(request, BodyHandlers.ofString());
					assertThat(response.statusCode(), is(200));
					assertThat(response.body(), containsString("newBalance"));
				}
				
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} finally {
				countDown.countDown();
			}
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		
		for (int i = 0; i < threads; i++) {
			executor.execute(transferAmountTask);
		}
		
		countDown.await();
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(900.0));
		assertThat(toDetail.getAccount().getBalance(), is(100.0));
	}
	
	@Test
	public void transfer_validRequestConcurrentUntilNoFunds() throws IOException, InterruptedException {
		
		Long from = ClientCreateRestHelper.createClient(PORT, "Frank", "500");
		Long to = ClientCreateRestHelper.createClient(PORT, "Alice", "0");
		
		String body = "{\r\n" + 
							"\"amount\" : 1," + 
							"\"toClientId\" : \"" + to + "\"" +
						"}";
		
		int numberOfTransfers = 510;
		
		CountDownLatch countDown = new CountDownLatch(numberOfTransfers);
		
		AtomicInteger fails = new AtomicInteger(0);
		
		Runnable transferAmountTask = () -> {
			HttpClient httpClient = HttpClient.newBuilder().build();
			
			HttpRequest request = createRequest(from, body);
			
			HttpResponse<String> response;
			try {
				response = httpClient.send(request, BodyHandlers.ofString());
				
				if (response.statusCode() == 400)
					fails.incrementAndGet();
				else {
					assertThat(response.statusCode(), is(200));
					assertThat(response.body(), containsString("newBalance"));
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} finally {
				countDown.countDown();
			}
		};
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		for (int i = 0; i < numberOfTransfers; i++) {
			executor.execute(transferAmountTask);
		}
		
		countDown.await();
		
		ClientDetailResponse fromDetail = ClientDetailRestHelper.getDetail(PORT, from);
		ClientDetailResponse toDetail = ClientDetailRestHelper.getDetail(PORT, to);
		
		assertThat(fromDetail.getAccount().getBalance(), is(0.0));
		assertThat(toDetail.getAccount().getBalance(), is(500.0));
		assertThat(fails.get(), is(10));
	}
	
	private HttpResponse<String> sendRequest(Long from, String body) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newBuilder().build();
		
		HttpRequest request = createRequest(from, body);
		
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		return response;
	}

	private HttpRequest createRequest(Long clientId, String body) {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + PORT + "/clients/" + clientId + "/accounts/transactions"))
				.timeout(Duration.ofMinutes(1))
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(body))
				.build();
		return request;
	}
}