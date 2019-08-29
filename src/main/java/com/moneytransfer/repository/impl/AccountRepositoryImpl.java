package com.moneytransfer.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.HibernateSessionManager;
import com.moneytransfer.repository.entity.Account;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class AccountRepositoryImpl implements AccountRepository {
	
	@Override
	public boolean transferFunds(Account accountFrom, Account accountTo) {

		Transaction transaction = null;
		try {
			final Session session = HibernateSessionManager.getSession();

			transaction = session.beginTransaction();

			session.saveOrUpdate(accountFrom);
			session.saveOrUpdate(accountTo);

			transaction.commit();
			
			session.close();

			return true;
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw ex;
		}
	}
}