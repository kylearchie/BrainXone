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
	private int creatorID;
	private String category = "";
	private ArrayList<Review> reviews;
	private boolean isSinglePage = true;

	/**
	 * Constructor: To be used by creator
	 * @param ID
	 * @param description
	 * @param creatorID
	 * @param category
	 */
	public Quiz(boolean isPlayerMode, String description, int creatorID, String category){
		if(!isPlayerMode) ID++;
		this.description = description;
		this.creatorID = creatorID;
		this.category = category;

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
			stmt.executeUpdate("INSERT INTO quiz VALUES (\"" + ID +"\",\"" + creatorID + "\",\"" + description + "\",\"" + category + "\");");
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

	public void singleOrMultiPage(boolean isSingle) {
		isSinglePage = isSingle;
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
			ResultSet rs = stmt.executeQuery("SELECT TOP " + n + " userID, score, timeTaken FROM quizPlayer WHERE quizID = " + ID + " ORDER BY score DESC, timeTaken ASC;");
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
		int creatorID = 0;
		String description = "", category = "";
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT creatorID, description, category FROM quiz WHERE quizID = \"" + quizID + "\"");
			if(rs.next()){				
				creatorID = Integer.parseInt(rs.getString(1));
				description = rs.getString(2);
				category = rs.getString(3);
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Quiz q = new Quiz(true, description, creatorID, category);
		return q;
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
}
