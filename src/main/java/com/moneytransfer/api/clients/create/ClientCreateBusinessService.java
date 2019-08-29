package com.moneytransfer.api.clients.create;

import com.moneytransfer.api.BusinessService;
import com.moneytransfer.mappers.ProjectMappers;
import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientCreateBusinessService implements BusinessService<ClientCreateRequest, ClientCreateResponse> {

	private ClientRepository repository;

	public ClientCreateBusinessService(ClientRepository repository) {
		super();
		this.repository = repository;
	}

	public ClientCreateResponse execute(ClientCreateRequest request) {
		Client client = ProjectMappers.CLIENT_CREATE_MAPPER.toClient(request);

		repository.save(client);

		return ProjectMappers.CLIENT_CREATE_MAPPER.toResponse(client);
	}
}
