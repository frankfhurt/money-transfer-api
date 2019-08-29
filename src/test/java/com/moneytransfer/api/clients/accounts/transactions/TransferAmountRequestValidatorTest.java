package com.moneytransfer.api.clients.accounts.transactions;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.moneytransfer.api.RestRequest;
import com.moneytransfer.common.Violation;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Account;
import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class TransferAmountRequestValidatorTest {

	TransferAmountRequestValidator validator;
	
	ClientRepository repository;
	
	@Before
	public void setUp() {
		repository = Mockito.mock(ClientRepository.class);
		validator = new TransferAmountRequestValidator(repository);
	}
	
	@Test
	public void validate_validRequest() {
	
		when(repository.findById(anyLong())).thenReturn(getClient());
		
		List<Violation> violations = validator.validate(TransferAmountRequest.builder().fromClientId(1L).amount("10").toClientId("2").build());
		
		assertTrue(violations.isEmpty());
	}
	
	@Test
	public void validate_invalidClientId() {
		
		when(repository.findById(2L)).thenReturn(getClient());
		
		List<Violation> violations = validator.validate(TransferAmountRequest.builder().fromClientId(1L).amount("10").toClientId("2").build());
		
		assertFalse(violations.isEmpty());
		assertThat(violations, hasItem(new Violation("clientId", "Client Not Found")));
	}
	
	@Test
	public void validate_toClientIdNotFound() {
		
		when(repository.findById(1L)).thenReturn(getClient());
		
		List<Violation> violations = validator.validate(TransferAmountRequest.builder().fromClientId(1L).amount("10").toClientId("2").build());
		
		assertFalse(violations.isEmpty());
		assertThat(violations, hasItem(new Violation("toClientId", "Client Not Found")));
	}
	
	@Test
	public void validate_insufficientFund() {
		
		when(repository.findById(anyLong())).thenReturn(getClient());
		
		List<Violation> violations = validator.validate(TransferAmountRequest.builder().fromClientId(1L).amount("20").toClientId("2").build());
		
		assertFalse(violations.isEmpty());
		assertThat(violations, hasItem(new Violation("balance", "Insufficient fund")));
	}
	
	@Test
	public void validate_sameClientIds() {
	
		when(repository.findById(anyLong())).thenReturn(getClient());
		
		List<Violation> violations = validator.validate(TransferAmountRequest.builder().fromClientId(1L).amount("10").toClientId("1").build());
		
		assertFalse(violations.isEmpty());
		assertThat(violations, hasItem(new Violation("toClientId", "clientId can't be equals toClientId")));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void validate_invalidRequestClass() {
		
		when(repository.findById(anyLong())).thenReturn(getClient());
		
		validator.validate(new InvalidRequestClass());
	}
	
	private Client getClient() {
		
		Account account = new Account();
		account.setId(1L);
		account.setBalance(new BigDecimal("10"));
		
		Client client = new Client();
		client.setId(1L);
		client.setName("Frank");
		client.setDocument("doc1234");
		client.setAccount(account);
		
		return client;
	}
	
	private class InvalidRequestClass extends RestRequest {
		
	}

}
