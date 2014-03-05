package brainxone;

import java.sql.*;
import java.util.ArrayList;

public class Challenge extends Message
{
	private int fromID;
	private int toID;
	private String timeSent;
	private String type;
	private String text;
	private int quizID;

	public Challenge(int from, int to, String t, String body, int quiz, Statement stmt) {	
		super(from, to, t, body);		
		timeSent = "" + System.currentTimeMillis();
		quizID = quiz;
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(" + fromID + "," + toID + ",\"" + 
		timeSent + "\",\"" + type + "\",\"" + text + "\"," + quizID + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getQuizID() 
	{
		return quizID;
	}
	
	public Challenge(int from, int to, String time, String t, String body, int quiz) {
		super(from, to, time, t, body);
		quizID = quiz;
	}
	
	public static ArrayList<Challenge> getChallenges(Integer userID, Statement stmt) {
		ArrayList<Challenge> challenges = new ArrayList<Challenge>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE toID = " + userID + 
					" AND messageType = \"challenge\" ORDER BY timeSent DECS;");
			while (rs.next()) {
		    	int fromID = rs.getInt("fromID");
		    	String timeSent = rs.getString("timeSent");
		    	String messageType = rs.getString("messageType");
		    	String text = rs.getString("text");
		    	int quizID = rs.getInt("quizID");
                Challenge challenge = new Challenge(fromID, userID, timeSent, messageType, text, quizID);
                challenges.add(challenge);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return challenges;
	}
}