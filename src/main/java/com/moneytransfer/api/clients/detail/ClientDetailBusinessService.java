package com.moneytransfer.api.clients.detail;

import javax.ws.rs.core.Response.Status;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.common.ErrorResponse;
import com.moneytransfer.common.exception.CustomWebApplicationException;
import com.moneytransfer.mappers.ProjectMappers;
import com.moneytransfer.repository.entity.Client;
import com.moneytransfer.repository.impl.ClientRepositoryImpl;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientDetailBusinessService implements BusinessService<ClientDetailRequest, ClientDetailResponse> {

	private ClientRepositoryImpl repository = new ClientRepositoryImpl();

	public ClientDetailResponse execute(ClientDetailRequest request) {

		Client client = repository.getById(request.getClientId());

		if (client == null)
			throw new CustomWebApplicationException(Status.NOT_FOUND, new ErrorResponse("Client Not Found"));

		return ProjectMappers.CLIENT_DETAIL_MAPPER.toClientDetailResponse(client);
	}

}
