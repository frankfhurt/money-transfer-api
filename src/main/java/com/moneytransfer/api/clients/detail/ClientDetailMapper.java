package com.moneytransfer.api.clients.detail;

import org.mapstruct.Mapper;

import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
@Mapper
public interface ClientDetailMapper {

	ClientDetailResponse toClientDetailResponse(Client client);
	
}
