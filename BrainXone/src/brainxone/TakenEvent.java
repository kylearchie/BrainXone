package brainxone;

import java.sql.*;
import java.util.ArrayList;

public class TakenEvent extends Event
{
	private String timeCreated;
	private String userName;
	private int quizID;
	private int score;
	private String timeTaken;

	public TakenEvent(String userName, int quiz, int points, String taken, Statement stmt) 
	{
		super(userName, quiz);
		timeCreated = "" + System.currentTimeMillis();
		score = points;
		timeTaken = taken;
		try {
			stmt.executeUpdate("INSERT INTO events VALUES(\"" + timeCreated + "\",\"" + userName + "\"," + 
					quizID + "," + score + ",\"" +  timeTaken + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public TakenEvent(String time, String userName, int quizID, int score, String timeTaken) {
	    super(time, userName, quizID);
		this.score = score;
		this.timeTaken = timeTaken;
	}

	public int getScore() {
		return score;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public static ArrayList<TakenEvent> getTakenEventss(String userName, Statement stmt) {
		ArrayList<TakenEvent> takenEvents = new ArrayList<TakenEvent>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE socre <> NULL and timeTaken <> NULL and userName = \"" + userName + "\" ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
		    	int score = rs.getInt("score");
		    	String timeTaken = rs.getString("timeTaken");
                TakenEvent takenEvent = new TakenEvent(time, userName, quizID, score, timeTaken);
                takenEvents.add(takenEvent);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return takenEvents;
	}
	
}