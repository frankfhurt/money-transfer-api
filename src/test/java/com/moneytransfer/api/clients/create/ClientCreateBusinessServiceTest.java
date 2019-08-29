package com.moneytransfer.api.clients.create;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
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
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Account;
import com.moneytransfer.repository.entity.Client;
import com.moneytransfer.repository.impl.ClientRepositoryImpl;

public class ClientCreateBusinessServiceTest {

	private ClientRepository mockRepository;

	private ClientRepository repository;

	private BusinessService<ClientCreateRequest, ClientCreateResponse> business;

	@Before
	public void setUp() {
		mockRepository = Mockito.mock(ClientRepository.class);
		repository = new ClientRepositoryImpl();
	}

	@Test
	public void execute_validRequest() {
		business = new ClientCreateBusinessService(repository);

		ClientCreateRequest request = getValidRequest();

		ClientCreateResponse response = business.execute(request);

		assertNotNull(response);
		assertThat(response.getId(), isA(Long.class));

		Client client = repository.findById(response.getId());

		assertNotNull(client);
		assertEquals(new BigDecimal(request.getInitialDeposit()), client.getAccount().getBalance());
	}

	@Test
	public void execute_validRequestWithMockRepository() {
		when(mockRepository.save(any())).thenReturn(getValidClient());

		business = new ClientCreateBusinessService(mockRepository);

		ClientCreateRequest request = getValidRequest();

		ClientCreateResponse response = business.execute(request);

		verify(mockRepository, times(1)).save(any(Client.class));
		assertNotNull(response);
	}

	private ClientCreateRequest getValidRequest() {
		return new ClientCreateRequest("Frank", "DOC1234", "10.00");
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

}
