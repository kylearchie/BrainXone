package brainxone;

import java.sql.*;
import java.util.*;

public class Message 
{
	private String fromID;
	private String toID;
	private String timeSent;
	private String type;
	private String text;

	public Message(String from, String to, String t, String body, Statement stmt)
	{
		fromID = from;
		toID = to;
		timeSent = "" + System.currentTimeMillis();
		type = t;
		text = body;
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(\"" + fromID + "\",\"" + toID + "\",\"" + 
		timeSent + "\",\"" + type + "\",\"" + text + "\", NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Message(String from, String to, String time, String t, String body)
	{
		fromID = from;
		toID = to;
		timeSent = time;
		type = t;
		text = body;
	}
	
	public Message(String from, String to, String t, String body)
	{
		fromID = from;
		toID = to;
		type = t;
		text = body;
	}
	
	

	public String getFromID() {
		return fromID;
	}

	public String getToID() {
		return toID;
	}

	public String getTimeSent() {
		return timeSent;
	}

	public String getType() {
		return type;
	}

	public String getText() {
		return text;
	}
	
	public static ArrayList<Message> getMessages(String userID, Statement stmt) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE messageType = \"message\" AND toUserName = \"" + userID + "\" ORDER BY timeSent DESC;");
			while (rs.next()) {
		    	String fromID = rs.getString("fromUserName");
		    	String timeSent = rs.getString("timeSent");
		    	String messageType = rs.getString("messageType");
		    	String text = rs.getString("text");
                Message message = new Message(fromID, userID, timeSent, messageType, text);
                messages.add(message);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return messages;
	}
	
	public static void deleteFriendRequest(String userName, String friendUserName, String timeSent,Statement stmt) {
		try {
			stmt.executeUpdate("DELETE FROM messages WHERE messageType = \"friend\" AND toUserName = \"" + userName + "\" AND fromUserName = \"" + friendUserName + "\" AND timeSent = \"" + timeSent + "\";");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}	    
	}
	
	
	
	
	public static ArrayList<Message> getFriendRequests(String userName, Statement stmt) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE messageType = \"friend\" AND toUserName = \"" + userName + "\" ORDER BY timeSent DESC;");
			while (rs.next()) {
		    	String fromUserName = rs.getString("fromUserName");
		    	String timeSent = rs.getString("timeSent");
		    	String messageType = rs.getString("messageType");
		    	String text = rs.getString("text");
                Message message = new Message(fromUserName, userName, timeSent, messageType, text);
                messages.add(message);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return messages;
	}
}
