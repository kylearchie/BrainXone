package brainxone;

import java.sql.*;
import java.util.ArrayList;

public class Challenge extends Message
{
	private String fromID;
	private String toID;
	private String timeSent;
	private String type;
	private String text;
	private int quizID;
	private boolean hasBeenRead;
	

	public Challenge(String from, String to, String t, String body, int quiz, Statement stmt) {	
		super(from, to, t, body, false);		
		timeSent = "" + System.currentTimeMillis();
		quizID = quiz;
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(\"" + fromID + "\",\"" + toID + "\",\"" + 
		timeSent + "\",\"" + type + "\",\"" + text + "\"," + quizID + ", NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getQuizID() 
	{
		return quizID;
	}
	
	public Challenge(String from, String to, String time, String t, String body, int quiz, boolean hasBeenRead) {
		super(from, to, time, t, body, hasBeenRead);
		quizID = quiz;
	}
	
	public static ArrayList<Challenge> getChallenges(String userName, Statement stmt) {
		ArrayList<Challenge> challenges = new ArrayList<Challenge>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE toUserName = \"" + userName + 
					"\" AND messageType = \"challenge\" ORDER BY timeSent DESC;");
			while (rs.next()) {
		    	String fromUserName = rs.getString("fromUserName");
		    	String timeSent = rs.getString("timeSent");
		    	String messageType = rs.getString("messageType");
		    	String text = rs.getString("text");
		    	int quizID = rs.getInt("quizID");
		    	boolean hasBeenRead = rs.getBoolean("hasBeenRead");
                Challenge challenge = new Challenge(fromUserName, userName, timeSent, messageType, text, quizID, hasBeenRead);
                challenges.add(challenge);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return challenges;
	}
	
	public static void deleteChallenge(String userName, String friendUserName, String timeSent,Statement stmt) {

		try {
			stmt.executeUpdate("DELETE FROM messages WHERE messageType = \"challenge\" AND toUserName = \"" + userName + "\" AND fromUserName = \"" + friendUserName + "\" AND timeSent = \"" + timeSent + "\";");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}	    
	}
}