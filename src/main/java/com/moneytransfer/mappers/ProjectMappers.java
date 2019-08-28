package com.moneytransfer.mappers;

import org.mapstruct.factory.Mappers;

import com.moneytransfer.api.clients.create.ClientCreateMapper;
import com.moneytransfer.api.clients.detail.ClientDetailMapper;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ProjectMappers {

	private ProjectMappers() {
		super();
	}

	public static final ClientCreateMapper CLIENT_CREATE_MAPPER = Mappers.getMapper(ClientCreateMapper.class);
	public static final ClientDetailMapper CLIENT_DETAIL_MAPPER = Mappers.getMapper(ClientDetailMapper.class);

}
