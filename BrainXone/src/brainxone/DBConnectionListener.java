package brainxone;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.sql.*;


/**
 * Application Lifecycle Listener implementation class DBConnectionListener
 *
 */
@WebListener
public class DBConnectionListener implements ServletContextListener {
	private DBConnection con;
    /**
     * Default constructor. 
     */
    public DBConnectionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	con = new DBConnection();
    	Statement stmt = con.getStatement();
        ServletContext servletContext = arg0.getServletContext();
    	servletContext.setAttribute("Statement", stmt);   
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        con.destroy();
    }
	
}
