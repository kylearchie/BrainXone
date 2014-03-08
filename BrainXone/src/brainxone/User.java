package brainxone;

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;


public class User {
	private String userName;
	private boolean isAdmin;
	private String password;
	private ArrayList<String> friends;
	
	public User(Statement stmt, String userName, String password) {
        this.userName = userName;
        isAdmin = false;
        friends = new ArrayList<String>();
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
			stmt.executeUpdate("INSERT INTO users VALUES(\"" + userName +
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
			rs = stmt.executeQuery("SELECT password FROM users WHERE userName = \"" + account + "\";");			
			while (rs.next()) {
		    	realPassword = rs.getString("password");
				size++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(account + " " + realPassword + " " + attemptedPassword);
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
		
	
	public User(String userName, boolean isAdmin, ArrayList<String> friends) {
		this.userName = userName;
		this.isAdmin = isAdmin;
		this.friends = friends;
	}
	
	public static User retrieveByUserName(String userName, Statement stmt) {
		User user = null;
		try {
			boolean RetrievedIsAdmin = false;
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userName = \"" + userName + "\";");
		    while (rs.next()) {
		    	RetrievedIsAdmin = rs.getBoolean("isAdmin");
		    }
		    ArrayList<String> friends = new ArrayList<String>();
		    rs = stmt.executeQuery("SELECT userName2 FROM friends WHERE userName1 = \"" + userName + "\";");
		    while (rs.next()) {
		    	String friendUserName = rs.getString("userName2");
		    	friends.add(friendUserName);
		    }
		    user = new User(userName, RetrievedIsAdmin, friends);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return user;
	}
	
	public static ArrayList<User> retrieveByPartialUserName(String userName, Statement stmt) {
		ArrayList<User> users = new ArrayList<User>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userName LIKE \"%" + userName + "%\";");
		    while (rs.next()) {
		    	String retrievedUserName = rs.getString("userName");
		    	boolean retrievedIsAdmin = rs.getBoolean("isAdmin");
		    	ArrayList<String> friends = new ArrayList<String>();
		    	ResultSet rsFriend = stmt.executeQuery("SELECT userName2 FROM friends WHERE userName1 = \"" + retrievedUserName + "\";");
				while (rsFriend.next()) {
					String friendUserName = rsFriend.getString("userName2");
				    friends.add(friendUserName);
				}
		    	users.add(new User(retrievedUserName, retrievedIsAdmin, friends));
		    }		   
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return users;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public boolean addFriend(String friendUserName, Statement stmt) {
		if (!friends.contains(friendUserName)) {
			friends.add(friendUserName);
			try {
				stmt.executeUpdate("INSERT INTO friends VALUES(\"" + userName + "\",\"" + friendUserName +"\");");
				stmt.executeUpdate("INSERT INTO friends VALUES(\"" + friendUserName + "\",\"" + userName +"\");");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeFriend(String friendUserName, Statement stmt) {
		if(friends.contains(friendUserName)) {
			friends.remove(friendUserName);
			try {
				stmt.executeUpdate("DELETE FROM friends WHERE userName1 = \"" + userName + "\"AND userName2 = \"" + friendUserName + "\";");
				stmt.executeUpdate("DELETE FROM friends WHERE userName1 = \"" + friendUserName + "\"AND userName2 = " + userName + "\";");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public ArrayList<String> getFriends() {
		return friends;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void promote(Statement stmt) {
		isAdmin = true;
		try {
			stmt.executeUpdate("UPDATE users SET isAdmin = TRUE WHERE userName = \"" + userName + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void demote(Statement stmt) {
		isAdmin = false;
		try {
			stmt.executeUpdate("UPDATE users SET isAdmin = FALSE WHERE userName = \"" + userName + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
