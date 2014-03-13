package backend;

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
public class DBandQuizListener implements ServletContextListener {
	private DBConnection con;
    /**
     * Default constructor. 
     */
    public DBandQuizListener() {
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
    	try {
			ResultSet rs = stmt.executeQuery("SELECT quizID FROM quiz WHERE quizID = (SELECT MAX(quizID) FROM quiz);");
			while(rs.next()){
				String x = rs.getString(1);
				if(x == null)
					Quiz.initMinID(0);
				else
					Quiz.initMinID(Integer.parseInt(rs.getString(1)));
				System.out.println(rs.getString(1));
			}
			rs = stmt.executeQuery("SELECT MAX(quesID) FROM ques;");
			while(rs.next()){
				String x = rs.getString(1);
				if( x == null ) 
					Question.initMinID( 0 );
				else 
					Question.initMinID(Integer.parseInt(rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        con.destroy();
    }
	
}
