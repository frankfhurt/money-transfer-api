package com.moneytransfer.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public class HibernateSessionManager {

	private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
	
	private HibernateSessionManager() {
		super();
	}

	public static Session getSession() {
		final Session session = sessionFactory.openSession();
		return session;
	}

}
