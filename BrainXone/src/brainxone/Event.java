package brainxone;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;


public class Event 
{
	private String timeCreated;
	private String userName;
	private int quizID;

	public Event(String userName, int quiz, Statement stmt)
	{   
		Date now = new Date();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		timeCreated = formatter.format(now);
		this.userName = userName;
		quizID = quiz;
		try {
			stmt.executeUpdate("INSERT INTO events VALUES(\"" + timeCreated + "\",\"" + userName + "\"," + quizID + ", NULL, NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Event(String time, String userName, int quizID) {
		this.timeCreated = time;
		this.userName = userName;
		this.quizID = quizID;
	}
	
	public Event(String userName, int quizID) {
		this.userName = userName;
		this.quizID = quizID;
	}
	
	public String getTime() 
	{
		return timeCreated;
	}

	public String getUserName() 
	{
		return userName;
	}

	public int getQuizID() 
	{
		return quizID;
	}

	public static ArrayList<Event> getCreateEvents(String userName, Statement stmt) {
		ArrayList<Event> events = new ArrayList<Event>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE score is NULL and timeTaken is NULL and userName = \"" + userName + "\" ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
                Event event = new Event(time, userName, quizID);
                events.add(event);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return events;
	}
	
	public static ArrayList<Event> getFriendsCreateEvent(String userName, Statement stmt) {
		ArrayList<Event> events = new ArrayList<Event>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT events.* FROM events,friends WHERE events.score is NULL AND events.timeTaken is NULL AND friends.userName1 = \"" + userName + "\" AND friends.userName2 = events.userName ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
		    	String friendUserName = rs.getString("userName");
                Event event = new Event(time, friendUserName, quizID);
                events.add(event);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return events;
	}
	
	public static int NumberOfQuizesCreated(String userName, Statement stmt) {
		ArrayList<Event> events = new ArrayList<Event>();
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE score is NULL and timeTaken is NULL and userName = \"" + userName + "\";");
			while (rs.next()) {
		    	count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return count;
	}
	
	public static void UpdateCreateAchievements(String userName, Statement stmt) {
		int size = Event.getCreateEvents(userName, stmt).size();
		if (size == 1 && !checkAmateur(userName, stmt)) {
			try {
				stmt.executeUpdate("INSERT INTO achievements VALUES(\"" + userName + "\", \"Amateur Author\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (size == 5 && !checkProlific(userName, stmt)) {
			try {
				stmt.executeUpdate("INSERT INTO achievements VALUES(\"" + userName + "\", \"Prolific Author\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (size == 10 && !checkProdigious(userName, stmt)) {
			try {
				stmt.executeUpdate("INSERT INTO achievements VALUES(\"" + userName + "\", \"Prodigious Author\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}
	
	public static boolean checkAmateur(String userName, Statement stmt) {
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE achievement = \"Amateur Author\" AND userName = \"" + userName + "\";");
			while (rs.next()) {
				count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		return count==1;
	}
	
	public static boolean checkProlific(String userName, Statement stmt) {
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE achievement = \"Prolific Author\" AND userName = \"" + userName + "\";");
			while (rs.next()) {
				count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		return count==1;
	}
	
	public static boolean checkProdigious(String userName, Statement stmt) {
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE achievement = \"Prodigious Author\" AND userName = \"" + userName + "\";");
			while (rs.next()) {
				count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		return count==1;
	}
	
	public static ArrayList<Event> getRecentCreatedQuiz(Statement stmt) {
		ArrayList<Event> Events = new ArrayList<Event>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE score IS NULL AND timeTaken IS NULL ORDER BY timeCreated DESC LIMIT 10;");
			while (rs.next()) {
				String userName = rs.getString("userName");
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
		    	int score = rs.getInt("score");
		    	long timeTaken = rs.getLong("timeTaken");
                Event event = new TakenEvent(time, userName, quizID, score, timeTaken);
                Events.add(event);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return Events;
	}
	
	
	
}