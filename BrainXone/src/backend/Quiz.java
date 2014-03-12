package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {
	private static int ID = 0;
	private String description = "";
	private ArrayList<String> tags;
	private ArrayList<Question> questions;
	private String creatorName = "";
	private String category = "";
	private ArrayList<Review> reviews;
	private int isRandom = 0, isOnePage = 1, isPracticeMode = 0;

	/**
	 * Constructor: To be used by creator
	 * @param ID
	 * @param description
	 * @param creatorID
	 * @param category
	 */
	public Quiz(boolean isPlayerMode, String description, String creatorName, String category, int isRandom, int isOnePage, int isPracticeMode){
		if(!isPlayerMode) 
			ID++;
		this.description = description;
		this.creatorName = creatorName;
		this.category = category;
		this.isRandom = isRandom;
		this.isOnePage = isOnePage;
		this.isPracticeMode = isPracticeMode;

		questions = new ArrayList<Question>();
		reviews = new ArrayList<Review>();
		tags = new ArrayList<String>();
	}


	public static void initMinID(int oldID){
		ID = oldID;
	}
	
	public void pushToQuizDB(){
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("INSERT INTO quiz VALUES (\"" + ID +"\",\"" + creatorName + "\",\"" + description + "\",\"" + category + "\",\"" + isRandom + "\",\"" + isOnePage + "\",\"" + isPracticeMode + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public void addQuestionToDB(Question q, int type, int isOrdered){
		questions.add(q);
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("INSERT INTO ques VALUES (\"" + q.getID() +"\",\"" + ID + "\",\"" + type  +"\",\"" + q.getQuesText() +"\",\"" + isOrdered +"\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public void addReview(Review r){
		reviews.add(r);
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("INSERT INTO review VALUES (\"" + ID +"\",\"" + r.getID() + "\"," + r.getStars() + "\",\"" + r.getText() + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void addTagsToDB(String t){
		tags.add(t);
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("INSERT INTO tag VALUES (\"" + ID +"\",\"" + t + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public ArrayList<Player> getTopPlayers(int n){
		ArrayList<Player> topPlayers = new ArrayList<Player>();
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT TOP " + n + " userName, score, timeTaken FROM quizPlayer WHERE quizID = " + ID + " ORDER BY score DESC, timeTaken ASC;");
			if(rs != null){
				while(rs.next()){
					Player p = new Player(rs.getString(1), rs.getString(2), rs.getString(3));
					topPlayers.add(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return topPlayers;
	}

	public int getQuizID(){
		return ID;
	}


	public static Quiz getQuizUsingID(int quizID){
		String creatorName = "";
		String description = "", category = "";
		int isRandom = 0, isOnePage = 1, isPracticeMode = 0;
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT creatorUserName, description, category, isRandom, isOnepage, isPracticeMode FROM quiz WHERE quizID = \"" + quizID + "\"");
			if(rs.next()){				
				creatorName = rs.getString(1);
				description = rs.getString(2);
				category = rs.getString(3);
				isRandom = Integer.parseInt(rs.getString(4));
				isOnePage = Integer.parseInt(rs.getString(5));
				isPracticeMode = Integer.parseInt(rs.getString(6));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Quiz q = new Quiz(true, description, "1", category, isRandom, isOnePage, isPracticeMode);
		return q;
	}
	
	public String getCreatorName(){
		return creatorName;
	}

	public String getDescription(){
		return description;
	}
	// to be used as q.getQuestion // player mode
	public static ArrayList<Question> getQuesListUsingID(int quizID){
		ArrayList<Question> qList = new ArrayList<Question>();

		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT quesID, quesType, quesText, isOrdered FROM ques WHERE quizID = \"" + quizID + "\"");
			if(rs != null){
				while(rs.next()){
					int quesID = Integer.parseInt(rs.getString(1));
					int quesType = Integer.parseInt(rs.getString(2));
					String quesText = rs.getString(3);
					int isOrdered = Integer.parseInt(rs.getString(4));
					
					Question q = null;
					if(quesType % 2 == Question.MULTI_CHOICE) {
						q = new MultiChoice(true, quesID, quesType, quesText);
					} else if(quesType % 2 == Question.STRING_RESPONSE) {
						q = new StringResponse(true, quesID, quesType, quesText);
					}
					qList.add(q);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qList;
	}

	private class Player {

		private String userName;
		private int score;
		private long timeTaken;
		public Player(String userName, String score, String time){
			this.userName = userName;
			this.score = Integer.parseInt(score);
			this.timeTaken = Long.parseLong(time);
		}
		public String getUserName(){
			return userName;
		}
		public int getScore(){
			return score;
		}
		public long getTime(){
			return timeTaken;
		}
	}
	
	public int isRandomVal(){
		return isRandom;
	}
	
	public int hasPracticeMode(){
		return isPracticeMode;
	}
	
	public void deleteQuizByCreatorName(String creatorName){
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("DELETE FROM quiz WHERE creatorUserName = \"" + creatorName + "\"");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteQuizByQuizID(int quizID){
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("DELETE FROM quiz WHERE quizID = \"" + quizID + "\"");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
