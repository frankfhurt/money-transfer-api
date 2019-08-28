package com.moneytransfer.api.clients.create;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.moneytransfer.mappers.ProjectMappers;
import com.moneytransfer.repository.entity.Account;
import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientCreateMapperTest {

	@Test
	public void toClient_validRequest() {

		ClientCreateRequest clientCreateRequest = new ClientCreateRequest("Frank", "doc1234", "10.0");

		Client client = ProjectMappers.CLIENT_CREATE_MAPPER.toClient(clientCreateRequest);

		assertEquals(client.getName(), clientCreateRequest.getName());
		assertEquals(client.getDocument(), clientCreateRequest.getDocument());
		assertEquals(client.getAccount().getBalance(), new BigDecimal(clientCreateRequest.getInitialDeposit()));
	}
	
	@Test
	public void toClient_requestWithoutInitialDeposit() {
		
		ClientCreateRequest clientCreateRequest = new ClientCreateRequest();
		clientCreateRequest.setName("Frank");
		clientCreateRequest.setDocument("doc1234");
		
		Client client = ProjectMappers.CLIENT_CREATE_MAPPER.toClient(clientCreateRequest);
		
		assertEquals(client.getName(), clientCreateRequest.getName());
		assertEquals(client.getDocument(), clientCreateRequest.getDocument());
		assertEquals(client.getAccount().getBalance(), new BigDecimal("0"));
	}

	@Test
	public void toResponse_validClient() {

		Account account = new Account();
		account.setId(1L);
		account.setBalance(new BigDecimal("10.05"));
		Client client = new Client();
		client.setId(1L);
		client.setName("Frank");
		client.setDocument("doc1234");
		client.setAccount(account);

		ClientCreateResponse response = ProjectMappers.CLIENT_CREATE_MAPPER.toResponse(client);

		assertEquals(client.getName(), response.getName());
		assertEquals(client.getAccount().getBalance(), response.getAccount().getBalance());
		assertEquals(client, client.getAccount().getClient());
	}

}
