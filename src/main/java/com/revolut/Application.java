package com.revolut;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.revolut.constants.TransactionAppConstants;
import com.revolut.dao.TransactionDao;
// TODO: Auto-generated Javadoc

/**
 * The Class Application.
 *
 * @author Syed Zarak Farid
 */
public class Application 
{
    
    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(Application.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main( String[] args )
	{

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(TransactionAppConstants.JETTY_SERVER_PORT);
		jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(
				org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter(
				"jersey.config.server.provider.packages",
				TransactionAppConstants.CONTROLLER_PACKAGE);

		//initialize data in the H2 Database
		TransactionDao.intialize();
		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			logger.error("Exception in starting jetty");
			e.printStackTrace();
		} finally {
			jettyServer.destroy();
		}
	}
}
