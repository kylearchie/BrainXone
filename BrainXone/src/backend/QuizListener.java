package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class QuizListener
 *
 */
@WebListener
public class QuizListener implements ServletContextListener {

	/**
	 * Default constructor. 
	 */
	public QuizListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT quizID FROM quiz WHERE quizID = (SELECT MAX(quizID) FROM quiz);");
			while(rs.next()){
				Quiz.initMinID(Integer.parseInt(rs.getString(1)));
				System.out.println(rs.getString(1));
			}
			rs = stmt.executeQuery("SELECT MAX(quesID) FROM ques;");
			while(rs.next()){
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
		// TODO Auto-generated method stub
	}

}
