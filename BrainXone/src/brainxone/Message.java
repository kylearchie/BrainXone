package brainxone;

import java.sql.*;
import java.util.*;

public class Message 
{
	private int fromID;
	private int toID;
	private String timeSent;
	private String type;
	private String text;

	public Message(int from, int to, String t, String body, Statement stmt)
	{
		fromID = from;
		toID = to;
		timeSent = "" + System.currentTimeMillis();
		type = t;
		text = body;
		try {
			stmt.executeUpdate("INSERT INTO messages VALUES(" + fromID + "," + toID + ",\"" + 
		timeSent + "\",\"" + type + "\",\"" + text + "\", NULL);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Message(int from, int to, String time, String t, String body)
	{
		fromID = from;
		toID = to;
		timeSent = time;
		type = t;
		text = body;
	}
	
	public Message(int from, int to, String t, String body)
	{
		fromID = from;
		toID = to;
		type = t;
		text = body;
	}
	
	

	public int getFromID() {
		return fromID;
	}

	public int getToID() {
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
	
	public static ArrayList<Message> getMessages(Integer userID, Statement stmt) {
		ArrayList<Message> messages = new ArrayList<Message>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM messages WHERE toID = " + userID + " ORDER BY timeSent DESC;");
			while (rs.next()) {
		    	int fromID = rs.getInt("fromID");
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
}
