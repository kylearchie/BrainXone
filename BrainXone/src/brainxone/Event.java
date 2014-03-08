package brainxone;

import java.sql.*;
import java.util.ArrayList;

public class Event 
{
	private String timeCreated;
	private String userName;
	private int quizID;

	public Event(String userName, int quiz, Statement stmt)
	{
		timeCreated = "" + System.currentTimeMillis();
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

	public static ArrayList<Event> getEventss(String userName, Statement stmt) {
		ArrayList<Event> events = new ArrayList<Event>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE userName = \"" + userName + "\" ORDER BY timeCreated DESC;");
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
}