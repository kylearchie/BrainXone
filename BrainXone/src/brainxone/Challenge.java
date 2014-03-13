package brainxone;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
		Date now = new Date();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		timeSent = formatter.format(now);
		quizID = quiz;
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(\"" + from + "\",\"" + to + "\",\"" + 
		timeSent + "\",\"" + t + "\",\"" + body + "\"," + quiz + ", NULL);");
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
	
	public static ArrayList<Challenge> getReports(String userName,Statement stmt) {
		ArrayList<Challenge> challenges = new ArrayList<Challenge>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE toUserName = \"" + userName + 
					"\" AND messageType = \"report\" ORDER BY timeSent DESC;");
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
	
	public static void deleteReport(String reportUser, int quizID,Statement stmt) {
		ArrayList<String> admins = User.getAllAdminUserName(stmt);
		for (String admin: admins) {
			try {
				stmt.executeUpdate("DELETE FROM messages WHERE messageType = \"report\" AND toUserName = \"" + admin + "\" AND fromUserName = \"" + reportUser + "\" AND quizID = " + quizID + ";");		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}	    
		}
	}
	
}