package com.moneytransfer.repository.impl;

import org.hibernate.Session;

import com.moneytransfer.repository.ClientRepository;
import com.moneytransfer.repository.HibernateSessionManager;
import com.moneytransfer.repository.entity.Client;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class ClientRepositoryImpl implements ClientRepository {

	@Override
	public Client save(Client client) {

		final Session session = HibernateSessionManager.getSession();
		session.beginTransaction();
		session.save(client);
		session.getTransaction().commit();

		session.close();
		
		return client;
	}

	@Override
	public Client getById(Long id) {

		final Session session = HibernateSessionManager.getSession();
		
		Client client = session.get(Client.class, id);
		
		session.close();
		
		return client;
	}
}