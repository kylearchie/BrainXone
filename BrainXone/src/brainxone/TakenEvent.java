package brainxone;

import java.sql.*;
import java.util.ArrayList;

public class TakenEvent extends Event
{
	private String timeCreated;
	private int userID;
	private int quizID;
	private int score;
	private String timeTaken;

	public TakenEvent(int user, int quiz, int points, String taken, Statement stmt) 
	{
		super(user, quiz);
		timeCreated = "" + System.currentTimeMillis();
		score = points;
		timeTaken = taken;
		try {
			stmt.executeUpdate("INSERT INTO events VALUES(\"" + timeCreated + "\"," + userID + "," + 
					quizID + ", " + score + ",\"" +  timeTaken + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public TakenEvent(String time, int userID, int quizID, int score, String timeTaken) {
	    super(time, userID, quizID);
		this.score = score;
		this.timeTaken = timeTaken;
	}

	public int getScore() {
		return score;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public static ArrayList<TakenEvent> getTakenEventss(Integer userID, Statement stmt) {
		ArrayList<TakenEvent> takenEvents = new ArrayList<TakenEvent>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE socre <> NULL and timeTaken <> NULL and userID = " + userID + " ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
		    	int score = rs.getInt("score");
		    	String timeTaken = rs.getString("timeTaken");
                TakenEvent takenEvent = new TakenEvent(time, userID, quizID, score, timeTaken);
                takenEvents.add(takenEvent);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return takenEvents;
	}
	
}