package brainxone;

import java.util.*;
import java.sql.*;


public class User {
	private static int nextID = 0;
	private Integer userID;
	private String userName;
	private boolean isAdmin;
	private ArrayList<Integer> friends;
	
	public User(Statement stmt, String userName) {
        userID = ++nextID;
        this.userName = userName;
        isAdmin = false;
        friends = new ArrayList<Integer>();
		try {
			stmt.executeUpdate("INSERT INTO users VALUES(" + userID + "," + userName + "," + isAdmin +");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public User(Integer userID, String userName, boolean isAdmin, ArrayList<Integer> friends) {
		this.userID = userID;
		this.userName = userName;
		this.isAdmin = isAdmin;
		this.friends = friends;
	}
	
	public static User retrieveByID(Integer userID, Statement stmt) {
		User user = null;
		try {
			String retrievedUserName = null;
			boolean RetrievedIsAdmin = false;
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userID = " + userID + ";");
		    while (rs.next()) {
		    	retrievedUserName = rs.getString("userName");
		    	RetrievedIsAdmin = rs.getBoolean("isAdmin");
		    }
		    ArrayList<Integer> friends = new ArrayList<Integer>();
		    rs = stmt.executeQuery("SELECT id2 FROM friends WHERE id1 = " + userID + ";");
		    while (rs.next()) {
		    	Integer friendID = rs.getInt("id2");
		    	friends.add(friendID);
		    }
		    user = new User(userID, retrievedUserName, RetrievedIsAdmin, friends);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return user;
	}
	
	public Integer getUserID() {
		return userID;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public boolean addFriend(Integer friendID, Statement stmt) {
		if (!friends.contains(friendID)) {
			friends.add(friendID);
			try {
				stmt.executeUpdate("INSERT INTO friends VALUES(" + userID + "," + friendID +");");
				stmt.executeUpdate("INSERT INTO friends VALUES(" + friendID + "," + userID +");");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeFriend(Integer friendID, Statement stmt) {
		if(friends.contains(friendID)) {
			friends.remove(friendID);
			try {
				stmt.executeUpdate("DELETE FROM friends WHERE id1 = " + userID + "AND id2 = " + friendID + ";");
				stmt.executeUpdate("DELETE FROM friends WHERE id1 = " + friendID + "AND id2 = " + userID + ";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<Integer> getFriends() {
		return friends;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void promote(Statement stmt) {
		isAdmin = true;
		try {
			stmt.executeUpdate("UPDATE users SET isAdmin = TRUE WHERE userID =" + userID + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demote(Statement stmt) {
		isAdmin = false;
		try {
			stmt.executeUpdate("UPDATE users SET isAdmin = FALSE WHERE userID =" + userID + ";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
