package brainxone;

import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import backend.DBConnection;


public class User {
	private String userName;
	private boolean isAdmin;
	private String password;
	private ArrayList<String> friends;
	private boolean isPrivate;
	
	public User(Statement stmt, String userName, String password) {
        this.userName = userName;
        isAdmin = false;
        friends = new ArrayList<String>();
        isPrivate = false;
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
					"\"," + isAdmin +",\"" + this.password + "\"," + isPrivate + ");");
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
		
	
	public User(String userName, boolean isAdmin, ArrayList<String> friends, boolean isPrivate) {
		this.userName = userName;
		this.isAdmin = isAdmin;
		this.friends = friends;
		this.isPrivate = isPrivate;
	}
	
	public static User retrieveByUserName(String userName, Statement stmt) {
		User user = null;
		try {
			boolean RetrievedIsAdmin = false;
			boolean RetrievedIsPrivate = false;
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE userName = \"" + userName + "\";");
		    while (rs.next()) {
		    	RetrievedIsAdmin = rs.getBoolean("isAdmin");
		    	RetrievedIsPrivate = rs.getBoolean("isPrivate"); 	
		    }
		    ArrayList<String> friends = new ArrayList<String>();
		    rs = stmt.executeQuery("SELECT userName2 FROM friends WHERE userName1 = \"" + userName + "\";");
		    while (rs.next()) {
		    	String friendUserName = rs.getString("userName2");
		    	friends.add(friendUserName);
		    }
		    user = new User(userName, RetrievedIsAdmin, friends, RetrievedIsPrivate);
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
		    	boolean retrievedIsPrivate = rs.getBoolean("isPrivate");
		    	ArrayList<String> friends = new ArrayList<String>();
		    	ResultSet rsFriend = stmt.executeQuery("SELECT userName2 FROM friends WHERE userName1 = \"" + retrievedUserName + "\";");
				while (rsFriend.next()) {
					String friendUserName = rsFriend.getString("userName2");
				    friends.add(friendUserName);
				}
		    	users.add(new User(retrievedUserName, retrievedIsAdmin, friends, retrievedIsPrivate));
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
				stmt.executeUpdate("DELETE FROM friends WHERE userName1 = \"" + userName + "\" AND userName2 = \"" + friendUserName + "\";");
				stmt.executeUpdate("DELETE FROM friends WHERE userName1 = \"" + friendUserName + "\" AND userName2 = \"" + userName + "\";");
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
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public void changePrivacy(Statement stmt) {
		isPrivate = !isPrivate;
		try {
			stmt.executeUpdate("UPDATE users SET isPrivate = " + isPrivate +" WHERE userName = \"" + userName + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	public static void DeleteUser(String userName, Statement stmt) {
		try {
			stmt.executeUpdate("DELETE from users WHERE userName = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from events WHERE userName = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from messages WHERE fromUserName = \"" + userName + "\" OR toUserName = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from friends WHERE userName1 = \"" + userName + "\" OR userName2 = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from reviews WHERE reviewUserName = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from quiz WHERE creatorUserName = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from quizPlayer WHERE userName = \"" + userName + "\";");
			stmt.executeUpdate("DELETE from achievements WHERE userName = \"" + userName + "\";");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getNumberOfUsers(Statement stmt) {
		int count = 0;
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM users;");
			while (rs.next()) {
		    	count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return count;
	}
	
	public static void deleteQuizByQuizID(int quizID, Statement stmt){
		try {
			stmt.executeUpdate("DELETE FROM quiz WHERE quizID = " + quizID + ";");
			stmt.executeUpdate("DELETE FROM events WHERE quizID = " + quizID + ";");
			stmt.executeUpdate("DELETE FROM messages WHERE quizID = " + quizID + ";");
			stmt.executeUpdate("DELETE FROM ques WHERE quizID = " + quizID + ";");
			stmt.executeUpdate("DELETE FROM tag WHERE quizID = " + quizID + ";");
			stmt.executeUpdate("DELETE FROM review WHERE quizID = " + quizID + ";");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getAllAdminUserName(Statement stmt) {
		ArrayList<String> users = new ArrayList<String>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT userName FROM users WHERE isAdmin = TRUE;");
		    while (rs.next()) {
		    	String adminName = rs.getString("userName");
		    	users.add(adminName);
		    }		   
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return users;
	}
	
	
	
}
