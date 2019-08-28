package com.moneytransfer.api.clients.create;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Mapper
public interface ClientCreateMapper {

	ClientCreateResponse toResponse(Client client);

	@Mapping(target = "account.balance", source = "initialDeposit", defaultValue = "0")
	Client toClient(ClientCreateRequest client);

}
