package brainxone;

import java.util.*;


public class User {
	private Integer userID;
	private String userName;
	private boolean isAdmin;
	private ArrayList<Integer> friends;
	
	public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
        isAdmin = false;
        friends = new ArrayList<Integer>();
    }
	
	public String getUserName() {
		return userName;
	}
	
	public boolean addFriend(Integer friendID) {
		if (!friends.contains(friendID)) {
			friends.add(friendID);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeFriend(Integer friendID) {
		if(friends.contains(friendID)) {
			friends.remove(friendID);
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
	
	public void promote() {
		isAdmin = true;
	}
	
	public void demote() {
		isAdmin = false;
	}
}
