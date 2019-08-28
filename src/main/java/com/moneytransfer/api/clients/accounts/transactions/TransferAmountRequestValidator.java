package com.moneytransfer.api.clients.accounts.transactions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.moneytransfer.api.RestRequest;
import com.moneytransfer.common.RequestValidator;
import com.moneytransfer.common.Violation;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class TransferAmountRequestValidator implements RequestValidator {

	private ClientRepository clientRepository;

	public TransferAmountRequestValidator(ClientRepository clientRepository) {
		super();
		this.clientRepository = clientRepository;
	}

	public List<Violation> validate(RestRequest request) {

		TransferAmountRequest transferAmountRequest;
		
		try {
			transferAmountRequest = (TransferAmountRequest) request;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Invalid RestRequest object");
		}
		
		List<Violation> violations = new ArrayList<>();

		Client source = clientRepository.getById(transferAmountRequest.getFromClientId());

		if (source == null)
			violations.add(new Violation("clientId", "Client Not Found"));
		else if (source.getAccount().getBalance().compareTo(new BigDecimal(transferAmountRequest.getAmount())) == -1)
			violations.add(new Violation("balance", "Insufficient fund"));

		Client target = clientRepository.getById(Long.valueOf(transferAmountRequest.getToClientId()));

		if (target == null)
			violations.add(new Violation("toClientId", "Client Not Found"));
		
		if (String.valueOf(transferAmountRequest.getFromClientId()).equals(transferAmountRequest.getToClientId()))
			violations.add(new Violation("toClientId", "clientId can't be equals toClientId"));

		return violations;
	}

}