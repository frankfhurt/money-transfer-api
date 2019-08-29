package com.moneytransfer.api.clients.detail;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.common.exception.CustomWebApplicationException;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Account;
import com.moneytransfer.repository.entity.Client;
import com.moneytransfer.repository.impl.ClientRepositoryImpl;

public class ClientDetailBusinessServiceTest {

	private ClientRepository mockRepository;

	private ClientRepository repository;

	private BusinessService<ClientDetailRequest, ClientDetailResponse> business;

	@Before
	public void setUp() {
		mockRepository = Mockito.mock(ClientRepository.class);
		repository = new ClientRepositoryImpl();
	}

	@Test
	public void execute_validRequest() {
		
		Client client = repository.save(getValidPersistableClient());
		
		business = new ClientDetailBusinessService(repository);

		ClientDetailRequest request = new ClientDetailRequest(client.getId());

		ClientDetailResponse response = business.execute(request);

		assertNotNull(response);
		assertThat(response.getId(), is(request.getClientId()));
	}
	
	@Test(expected = CustomWebApplicationException.class)
	public void execute_clientNotFound() {
		
		business = new ClientDetailBusinessService(repository);
		
		ClientDetailRequest request = new ClientDetailRequest(1L);
		
		business.execute(request);
	}

	@Test
	public void execute_validRequestWithMockRepository() {
		when(mockRepository.findById(any())).thenReturn(getValidClient());

		business = new ClientDetailBusinessService(mockRepository);

		ClientDetailRequest request = new ClientDetailRequest(1L);

		ClientDetailResponse response = business.execute(request);

		verify(mockRepository, times(1)).findById(any(Long.class));
		assertNotNull(response);
	}
	
	@Test(expected = CustomWebApplicationException.class)
	public void execute_clientNotFoundWithMockRepository() {
		when(mockRepository.findById(any())).thenReturn(null);
		
		business = new ClientDetailBusinessService(mockRepository);
		
		ClientDetailRequest request = new ClientDetailRequest(1L);
		
		business.execute(request);
	}

	private Client getValidClient() {
		Account account = new Account();
		account.setId(1L);
		account.setBalance(new BigDecimal("10.00"));

		Client client = new Client();
		client.setId(1L);
		client.setName("Frank");
		client.setDocument("DOC1234");
		client.setAccount(account);

		return client;
	}
	
	private Client getValidPersistableClient() {
		Account account = new Account();
		account.setBalance(new BigDecimal("10.00"));
		
		Client client = new Client();
		client.setName("Frank");
		client.setDocument("DOC1234");
		client.setAccount(account);
		
		return client;
	}

}
