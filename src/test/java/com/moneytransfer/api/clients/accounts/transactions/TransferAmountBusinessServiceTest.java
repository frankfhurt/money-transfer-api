package com.moneytransfer.api.clients.accounts.transactions;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.common.RequestValidator;
import com.moneytransfer.common.Violation;
import com.moneytransfer.common.exception.CustomViolationException;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Account;
import com.moneytransfer.repository.entity.Client;
import com.moneytransfer.repository.impl.AccountRepositoryImpl;
import com.moneytransfer.repository.impl.ClientRepositoryImpl;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class TransferAmountBusinessServiceTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private BusinessService<TransferAmountRequest, TransferAmountResponse> businessService;
	
	private ClientRepository clientRepository;
	
	private AccountRepository accountRepository;
	
	private RequestValidator validator;

	@Before
	public void setUp() {
		clientRepository = new ClientRepositoryImpl();
		accountRepository = new AccountRepositoryImpl();
		validator = new TransferAmountRequestValidator(clientRepository);
		this.businessService = new TransferAmountBusinessService(clientRepository, accountRepository, validator);
	}

	@Test
	public void execute_validRequest_shouldTransferAmounts() {

		Long clientIdSource = insertValidClient("User One", new BigDecimal("10"));
		Long clientIdTarget = insertValidClient("User Two", new BigDecimal("15"));

		TransferAmountRequest request = TransferAmountRequest.builder()
																.fromClientId(clientIdSource)
																.amount("10.00")
																.toClientId(clientIdTarget.toString())
															.build();

		businessService.execute(request);

		Client clientSource = clientRepository.findById(clientIdSource);
		Client clientTarget = clientRepository.findById(clientIdTarget);
		
		assertThat(clientSource.getAccount().getBalance(), is(new BigDecimal("0.00")));
		assertThat(clientTarget.getAccount().getBalance(), is(new BigDecimal("25.00")));
	}
	
	@Test
	public void executeConcurrent_validRequest_shouldTransferAmounts() throws InterruptedException {
		
		int numberOfTransfers = 2000;
		
		CountDownLatch countDown = new CountDownLatch(numberOfTransfers);
		
		Long clientIdSource = insertValidClient("User One", new BigDecimal("2000"));
		Long clientIdTarget = insertValidClient("User Two", new BigDecimal("0"));
		
		TransferAmountRequest request = TransferAmountRequest.builder()
																.fromClientId(clientIdSource)
																.amount("1.00")
																.toClientId(clientIdTarget.toString())
															.build();
		
		Runnable transferAmountTask = () -> {
			businessService.execute(request);
			countDown.countDown();
		};
	
		ExecutorService executor = Executors.newCachedThreadPool();
		
		for (int i = 0; i < numberOfTransfers; i++) {
			executor.execute(transferAmountTask);
		}
		
		countDown.await();
		
		Client clientSource = clientRepository.findById(clientIdSource);
		Client clientTarget = clientRepository.findById(clientIdTarget);
		
		assertThat(clientSource.getAccount().getBalance(), is(new BigDecimal("0.00")));
		assertThat(clientTarget.getAccount().getBalance(), is(new BigDecimal("2000.00")));
	}
	
	@Test
	public void executeConcurrentUntilNoFunds_validRequest_shouldTransferAmounts() throws InterruptedException {
		
		int numberOfTransfers = 2010;
		
		CountDownLatch countDown = new CountDownLatch(numberOfTransfers);
		
		Long clientIdSource = insertValidClient("User One", new BigDecimal("2000"));
		Long clientIdTarget = insertValidClient("User Two", new BigDecimal("0"));
		
		TransferAmountRequest request = TransferAmountRequest.builder()
				.fromClientId(clientIdSource)
				.amount("1.00")
				.toClientId(clientIdTarget.toString())
				.build();
		
		AtomicInteger fails = new AtomicInteger(0);
		
		Runnable transferAmountTask = () -> {
			try {
				businessService.execute(request);
			} catch (Exception e) {
				fails.incrementAndGet();
			} finally {
				countDown.countDown();
			}
		};
		
		ExecutorService executor = Executors.newCachedThreadPool();
		
		for (int i = 0; i < numberOfTransfers; i++) {
			executor.execute(transferAmountTask);
		}
		
		countDown.await();
		
		Client clientSource = clientRepository.findById(clientIdSource);
		Client clientTarget = clientRepository.findById(clientIdTarget);
		
		assertThat(clientSource.getAccount().getBalance(), is(new BigDecimal("0.00")));
		assertThat(clientTarget.getAccount().getBalance(), is(new BigDecimal("2000.00")));
		assertThat(fails.get(), is(10));
	}
	
	@Test
	public void execute_noFunds_CustomViolationExceptionThrown() {
		
		Violation violation = new Violation("balance", "Insufficient fund");

		thrown.expect(CustomViolationException.class);
		thrown.expect(hasProperty("violations", hasItem(violation)));
		
		Long clientIdSource = insertValidClient("User One", new BigDecimal("5"));
		Long clientIdTarget = insertValidClient("User Two", new BigDecimal("15"));
		
		TransferAmountRequest request = TransferAmountRequest.builder()
																.fromClientId(clientIdSource)
																.amount("10.00")
																.toClientId(clientIdTarget.toString())
															.build();
		businessService.execute(request);
	}
	
	@Test
	public void execute_invalidFromClientId_CustomViolationExceptionThrown() {

		Violation violation = new Violation("clientId", "Client Not Found");

		thrown.expect(CustomViolationException.class);
		thrown.expect(hasProperty("violations", hasItem(violation)));
		
		Long clientIdSource = 12L;
		Long clientIdTarget = insertValidClient("User Two", new BigDecimal("15"));
		
		TransferAmountRequest request = TransferAmountRequest.builder()
																.fromClientId(clientIdSource)
																.amount("10.00")
																.toClientId(clientIdTarget.toString())
															.build();
		businessService.execute(request);
	}
	
	@Test
	public void execute_invalidToClientId_CustomViolationExceptionThrown() {

		Violation violation = new Violation("toClientId", "Client Not Found");

		thrown.expect(CustomViolationException.class);
		thrown.expect(hasProperty("violations", hasItem(violation)));
		
		Long clientIdSource = insertValidClient("User One", new BigDecimal("5"));
		Long clientIdTarget = 12L;
		
		TransferAmountRequest request = TransferAmountRequest.builder()
																.fromClientId(clientIdSource)
																.amount("10.00")
																.toClientId(clientIdTarget.toString())
															.build();
		businessService.execute(request);
	}

	private Long insertValidClient(String name, BigDecimal balance) {

		Account account = new Account();
		account.setBalance(balance);
		Client client = new Client();
		client.setName(name);
		client.setDocument("doc1234");
		client.setAccount(account);

		clientRepository.save(client);

		return client.getId();
	}

}
