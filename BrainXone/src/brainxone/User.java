package brainxone;

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class User {
	private static int nextID = 0;
	private Integer userID;
	private String userName;
	private boolean isAdmin;
	private String password;
	private ArrayList<Integer> friends;
	
	public User(Statement stmt, String userName, String password) {
        userID = ++nextID;
        this.userName = userName;
        isAdmin = false;
        friends = new ArrayList<Integer>();
        MessageDigest mdigest;
        try {
			mdigest = MessageDigest.getInstance("SHA");
			byte[] inputBytes = password.getBytes();
			byte[] outputBytes = mdigest.digest(inputBytes);
			this.password = hexToString(outputBytes);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		try {
			stmt.executeUpdate("INSERT INTO users VALUES(" + userID + ",\"" + userName +
					"\"," + isAdmin +",\"" + this.password + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public static boolean passwordMatch(String account, String password, Statement stmt) {
		ResultSet rs;
		int size = 0;
		MessageDigest mdigest;
		String attemptedPassword = null;
		try {
			mdigest = MessageDigest.getInstance("SHA");
			byte[] inputBytes = password.getBytes();
			byte[] outputBytes = mdigest.digest(inputBytes);
			attemptedPassword = hexToString(outputBytes);			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		String realPassword = null;
		try {
			rs = stmt.executeQuery("SELECT password FROM accounts WHERE userName = \"" + account + "\";");			
			while (rs.next()) {
		    	realPassword = rs.getString("password");
				size++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		return size == 1 && attemptedPassword.equals(realPassword);
	}
	
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}	
	
	
	public static boolean userExist(String userName, Statement stmt){
		ResultSet rs;
		int size = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM users WHERE userName = \"" + userName + "\";");			
			while (rs.next()) {
				size++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return size == 1;
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
