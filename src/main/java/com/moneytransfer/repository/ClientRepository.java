package com.moneytransfer.repository;

import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public interface ClientRepository {

	Client save(Client client);
	
	Client getById(Long id);
	
}