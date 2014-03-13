package brainxone;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import backend.DBConnection;

public class TakenEvent extends Event
{
	private String timeCreated;
	private String userName;
	private int quizID;
	private int score;
	private long timeTaken;

	public TakenEvent(String userName, int quiz, int points, long taken, Statement stmt) 
	{
		super(userName, quiz);
		timeCreated = "" + System.currentTimeMillis();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String timeString = dateFormat.format(now);
		score = points;
		timeTaken = taken;
		try {
			stmt.executeUpdate("INSERT INTO events VALUES(\"" + timeString + "\",\"" + userName + "\"," + 
					quizID + "," + score + "," +  timeTaken + ");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public TakenEvent(String time, String userName, int quizID, int score, long timeTaken) {
	    super(time, userName, quizID);
		this.score = score;
		this.timeTaken = timeTaken;
	}

	public int getScore() {
		return score;
	}

	public double getTimeTaken() {
		return timeTaken;
	}

	public static ArrayList<TakenEvent> getTakenEvents(String userName, Statement stmt) {
		ArrayList<TakenEvent> takenEvents = new ArrayList<TakenEvent>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE score IS NOT NULL AND timeTaken IS NOT NULL AND userName = \"" + userName + "\" ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
		    	int score = rs.getInt("score");
		    	long timeTaken = rs.getLong("timeTaken");
                TakenEvent takenEvent = new TakenEvent(time, userName, quizID, score, timeTaken);
                takenEvents.add(takenEvent);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return takenEvents;
	}
	
	public static ArrayList<TakenEvent> getFriendsTakenEvents(String userName, Statement stmt) {
		ArrayList<TakenEvent> takenEvents = new ArrayList<TakenEvent>();
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT events.* FROM events,friends WHERE events.score IS NOT NULL AND timeTaken IS NOT NULL AND friends.userName1 = \"" + userName + "\" AND friends.userName2 = events.userName ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	String time = rs.getString("timeCreated");
		    	int quizID = rs.getInt("quizID");
		    	int score = rs.getInt("score");
		    	long timeTaken = rs.getLong("timeTaken");
                TakenEvent takenEvent = new TakenEvent(time, userName, quizID, score, timeTaken);
                takenEvents.add(takenEvent);
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return takenEvents;
	}
	
	public static int getNumberOfTakenEvents(Statement stmt) {
		int count = 0;
		ResultSet rs;
		try {
			rs = stmt.executeQuery("SELECT * FROM events WHERE score IS NOT NULL AND timeTaken IS NOT NULL ORDER BY timeCreated DESC;");
			while (rs.next()) {
		    	count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
		return count;
	}
	
	public static boolean checkMachine(String userName, Statement stmt) {
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE achievement = \"Quiz Machine\" AND userName = \"" + userName + "\";");
			while (rs.next()) {
				count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		return count==1;
	}
	
	public static void UpdateTakenAchievements(String userName, Statement stmt) {
		int size = TakenEvent.getTakenEvents(userName, stmt).size();
		if (size == 10 && !checkMachine(userName, stmt)) {
			try {
				stmt.executeUpdate("INSERT INTO achievements VALUES(\"" + userName + "\", \"Quiz Machine\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} 
	}
	
	public static void UpdateGreatestAchievements(String userName, Statement stmt) {
		try {
			stmt.executeUpdate("INSERT INTO achievements VALUES(\"" + userName + "\", \"I am the Greatest\");");
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	public static void UpdatePractivechievements(String userName, Statement stmt) {
		try {
			stmt.executeUpdate("INSERT INTO achievements VALUES(\"" + userName + "\", \"Practice Makes Perfect\");");
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public static boolean CheckQualifiedGreatest(String userName, int quizID, Statement stmt) {
		if(userName == null) return false;
		ResultSet rs;
		String numberOneUserName = null;
		try {
			rs = stmt.executeQuery("SELECT userName FROM events WHERE score IS NOT NULL AND timeTaken IS NOT NULL AND quizID = " + quizID + " ORDER BY score DESC, timeTaken ASC LIMIT 1");
			while (rs.next()) {
				numberOneUserName = rs.getString("userName");
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (numberOneUserName != null) {
			return userName.equals(numberOneUserName);
		}
		return true;
	}
		
	public static boolean checkPractice(String userName, Statement stmt) {
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE achievement = \"Practice Makes Perfect\" AND userName = \"" + userName + "\";");
			while (rs.next()) {
				count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		return count==1;
	}
	
	public static boolean checkGreatest(String userName, Statement stmt) {
		ResultSet rs;
		int count = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM achievements WHERE achievement = \"I am the Greatest\" AND userName = \"" + userName + "\";");
			while (rs.next()) {
				count++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    
	
		return count==1;
	}
	
	public static int bestScore(String userName, int quizID, Statement stmt) {
		int score = 0;
		try {
			ResultSet rs = stmt.executeQuery("SELECT score FROM events WHERE userName = \"" + userName + "\" AND quizID = " + quizID + "ORDER BY score DESC LIMIT 1;");
			while (rs.next()) {
				score = rs.getInt("score");
				
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
	}
	
	
	
	
}