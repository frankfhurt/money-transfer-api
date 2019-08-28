package com.moneytransfer.repository;

import com.moneytransfer.repository.entity.Account;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public interface AccountRepository {
	
	boolean transferFunds(Account accountFrom, Account accountTo);

}