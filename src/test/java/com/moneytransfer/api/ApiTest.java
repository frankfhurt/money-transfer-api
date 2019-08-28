package com.moneytransfer.api;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.moneytransfer.api.clients.accounts.transactions.TransferAmountRest;
import com.moneytransfer.api.clients.create.ClientCreateRest;
import com.moneytransfer.api.clients.detail.ClientDetailRest;
import com.moneytransfer.common.exception.ConstraintViolationHandler;
import com.moneytransfer.common.exception.CustomViolationHandler;

/**
 * 
 * @author Franklyn Vieira
 * @since 08/2019
 *
 */
public abstract class ApiTest {

	protected static final int PORT = 8089;
	
	protected static Server server;
	
	@BeforeClass
	public static void start() throws Exception {
		server = new Server(PORT);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
						TransferAmountRest.class.getCanonicalName() + "," 
						+ ClientCreateRest.class.getCanonicalName() + ","
						+ ClientDetailRest.class.getCanonicalName() + ","
						+ CustomViolationHandler.class.getCanonicalName() + ","
						+ ConstraintViolationHandler.class.getCanonicalName());
		server.start();
	}
	
	@AfterClass
	public static void shutDown() throws Exception {
		if (server != null && server.isStarted())
			server.stop();
	}

}
