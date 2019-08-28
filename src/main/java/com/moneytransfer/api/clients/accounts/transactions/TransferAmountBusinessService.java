package com.moneytransfer.api.clients.accounts.transactions;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.common.RequestValidator;
import com.moneytransfer.common.Violation;
import com.moneytransfer.common.exception.CustomViolationException;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class TransferAmountBusinessService implements BusinessService<TransferAmountRequest, TransferAmountResponse> {

	private static ReentrantLock accessLock = new ReentrantLock();

	private ClientRepository clientRepository;
	
	private AccountRepository accountRepository;
	
	private RequestValidator validator;

	public TransferAmountBusinessService(ClientRepository clientRepository, 
										AccountRepository accountRepository,
										RequestValidator validator) {
		super();
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
		this.validator = validator;
	}

	public TransferAmountResponse execute(TransferAmountRequest request) {
		
		accessLock.lock();

		Client source;
		Client target;
		
		try {

			List<Violation> violations = validator.validate(request);
			
			if (!violations.isEmpty())
				throw new CustomViolationException(violations);
			
			source = clientRepository.getById(request.getFromClientId());
			target = clientRepository.getById(Long.valueOf(request.getToClientId()));

			source.getAccount().subtractBalance(request.getAmount());
			target.getAccount().addBalance(request.getAmount());

			accountRepository.transferFunds(source.getAccount(), target.getAccount());

		} catch (Exception e) {
			throw e;
		} finally {
			accessLock.unlock();
		}

		return new TransferAmountResponse(source.getAccount().getBalance().toString());
	}
}