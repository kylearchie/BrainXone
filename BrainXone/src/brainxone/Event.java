package brainxone;

import java.sql.*;
import java.util.ArrayList;

public class Event 
{
	private String time;
	private int userID;
	private int quizID;

	public Event(int user, int quiz, Statement stmt)
	{
		time = "" + System.currentTimeMillis();
		userID = user;
		quizID = quiz;
		try {
			stmt.executeUpdate("INSERT INTO events VALUES(\"" + time + "\"," + userID + "," + quizID + ", NULL, NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Event(String time, int userID, int quizID) {
		this.time = time;
		this.userID = userID;
		this.quizID = quizID;
	}
	
	public Event(int userID, int quizID) {
		this.userID = userID;
		this.quizID = quizID;
	}
	
	public String getTime() 
	{
		return time;
	}

	public int getUserID() 
	{
		return userID;
	}

	public int getQuizID() 
	{
		return quizID;
	}

	public static ArrayList<Event> getEventss(Integer userID, Statement stmt) {
		ArrayList<Event> events = new ArrayList<Event>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE userID = " + userID + " ORDER BY time DESC;");
			while (rs.next()) {
		    	String time = rs.getString("time");
		    	int quizID = rs.getInt("quizID");
                Event event = new Event(time, userID, quizID);
                events.add(event);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return events;
	}
}