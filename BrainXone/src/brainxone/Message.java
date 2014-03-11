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
	private boolean hasBeenRead;

	public Message(String from, String to, String t, String body, Statement stmt)
	{
		fromID = from;
		toID = to;
		timeSent = "" + System.currentTimeMillis();
		type = t;
		text = body;
		hasBeenRead = false;
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(\"" + fromID + "\",\"" + toID + "\",\"" + 
		timeSent + "\",\"" + type + "\",\"" + text + "\", NULL," + hasBeenRead + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public Message(String from, String to, String time, String t, String body, boolean hasBeenRead)
	{
		fromID = from;
		toID = to;
		timeSent = time;
		type = t;
		text = body;
		this.hasBeenRead = hasBeenRead;
	}
	
	public Message(String from, String to, String t, String body, boolean hasBeenRead)
	{
		fromID = from;
		toID = to;
		type = t;
		text = body;
		this.hasBeenRead = hasBeenRead;
	}
	
	public static void addAnnouncement(String userName, String body, Statement stmt) {
		String timeSent = "" + System.currentTimeMillis();
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(\"" + userName + "\", NULL ,\"" + 
		timeSent + "\", \"announcement\" ,\"" + body + "\", NULL, NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Message> getAnnouncements(Statement stmt) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE messageType = \"announcement\" AND toUserName is NULL ORDER BY timeSent DESC;");
			while (rs.next()) {
		    	String fromID = rs.getString("fromUserName");
		    	String timeSent = rs.getString("timeSent");
		    	String messageType = rs.getString("messageType");
		    	String text = rs.getString("text");
		    	boolean hasBeenRead = rs.getBoolean("hasBeenRead");
		    	Message message = new Message(fromID, null , timeSent, messageType, text, hasBeenRead);
                messages.add(message);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return messages;
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
	
	public boolean getHasBeenRead() {
		return hasBeenRead;
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
		    	boolean hasBeenRead = rs.getBoolean("hasBeenRead");
		    	Message message = new Message(fromID, userID, timeSent, messageType, text, hasBeenRead);
                messages.add(message);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return messages;
	}
	
	public static ArrayList<Message> getUnReadMessages(String userID, Statement stmt) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE hasBeenRead = FALSE AND messageType = \"message\" AND toUserName = \"" + userID + "\" ORDER BY timeSent DESC;");
			while (rs.next()) {
		    	String fromID = rs.getString("fromUserName");
		    	String timeSent = rs.getString("timeSent");
		    	String messageType = rs.getString("messageType");
		    	String text = rs.getString("text");
		    	boolean hasBeenRead = rs.getBoolean("hasBeenRead");
                Message message = new Message(fromID, userID, timeSent, messageType, text, hasBeenRead);
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
		    	boolean hasBeenRead = rs.getBoolean("hasBeenRead");
                Message message = new Message(fromUserName, userName, timeSent, messageType, text, hasBeenRead);
                messages.add(message);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return messages;
	}
	
	public void setRead(Statement stmt) {
		this.hasBeenRead = true;
		try {
			stmt.executeUpdate("UPDATE messages SET hasBeenRead = TRUE WHERE messageType = \"message\" AND toUserName = \"" + this.toID + "\" AND fromUserName = \"" + this.fromID + "\" AND timeSent = \"" + this.timeSent + "\";");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}	    
	}
	
}
