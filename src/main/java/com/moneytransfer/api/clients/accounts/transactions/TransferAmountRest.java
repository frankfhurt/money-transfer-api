package com.moneytransfer.api.clients.accounts.transactions;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.common.RequestValidator;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.impl.AccountRepositoryImpl;
import com.moneytransfer.repository.impl.ClientRepositoryImpl;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Path("/clients/{clientId}/accounts/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferAmountRest {
	
	private ClientRepository clientRepository = new ClientRepositoryImpl();
	
	private AccountRepository accountRepository = new AccountRepositoryImpl();
	
	private RequestValidator validator = new TransferAmountRequestValidator(clientRepository);

	BusinessService<TransferAmountRequest, TransferAmountResponse> businessService = new TransferAmountBusinessService(clientRepository, accountRepository, validator);

	@POST
	public Response transfer(@PathParam("clientId")
							Long clientId, 
							@Valid
							@NotNull(message = "request cannot be null")
							TransferAmountRequest request) {
		
		request.setFromClientId(clientId);

		TransferAmountResponse response = businessService.execute(request);
		
		return Response.status(Status.OK).entity(response).build();
	}

}
