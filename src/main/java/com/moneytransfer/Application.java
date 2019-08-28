package com.moneytransfer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

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
public class Application {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
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
		try {
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}

}
