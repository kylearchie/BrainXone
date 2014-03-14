package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {
	private static int ID = 0;
	private String quizName = "";
	private String description = "";
	private ArrayList<Question> questions;
	private String creatorName = "";
	private String category = "";
	private int isRandom = 0, isOnePage = 1, isPracticeMode = 0;

	/**
	 * Constructor: To be used by creator
	 * @param ID
	 * @param description
	 * @param creatorID
	 * @param category
	 */
	public Quiz(boolean isPlayerMode, String quizName, String description, String creatorName, String category, int isRandom, int isOnePage, int isPracticeMode){
		if(!isPlayerMode) 
			ID++;
		this.description = description;
		this.quizName = quizName;
		this.creatorName = creatorName;
		this.category = category;
		this.isRandom = isRandom;
		this.isOnePage = isOnePage;
		this.isPracticeMode = isPracticeMode;
		questions = new ArrayList<Question>();
	}


	public static void initMinID(int oldID){
		ID = oldID;
	}
	
	public void pushToQuizDB(Statement stmt){
		try {
			stmt.executeUpdate("INSERT INTO quiz VALUES (\"" + ID +"\",\"" + creatorName + "\",\"" + quizName + "\",\"" + description + "\",\"" + category + "\",\"" + isRandom + "\",\"" + isOnePage + "\",\"" + isPracticeMode + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public void addQuestionToDB(Question q, int type, int isOrdered, Statement stmt){
		questions.add(q);
		try {
			stmt.executeUpdate("INSERT INTO ques VALUES (\"" + q.getID() +"\",\"" + ID + "\",\"" + type  +"\",\"" + q.getQuesText() +"\",\"" + isOrdered +"\",\"" + q.getMaxPoints(stmt) +"\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public static void addReviewAndRating(int quizID, String reviewerName, String textReview, int stars, Statement stmt){
		try {
			String sql = "INSERT INTO review VALUES (\"" + quizID +"\",\"" + reviewerName + "\",\"" + stars + "\",\"" + textReview + "\");";
			System.out.println(sql);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Review> getReviewByQuizID(int quizID, Statement stmt){
		ArrayList<Review> reviews = new ArrayList<Review>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT reviewUserName, stars, text FROM review WHERE quizID = \"" + quizID + "\";");
			while(rs.next()){				
				String reviewerName = rs.getString(1);
				int stars = Integer.parseInt(rs.getString(2));
				String textReview = rs.getString(3);
				Review r = new Review(stars, textReview, reviewerName);
				reviews.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}


	public void addTagsToDB(String t, Statement stmt){
		try {
			stmt.executeUpdate("INSERT INTO allTags VALUES (\"" + ID +"\",\"" + t + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public ArrayList<Player> getTopPlayers(int n, Statement stmt){
		ArrayList<Player> topPlayers = new ArrayList<Player>();
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


	public static Quiz getQuizUsingID(int quizID, Statement stmt){
		if(quizID == 0) return null;
		String creatorName = "";
		String quizName = "", description = "", category = "";
		int isRandom = 0, isOnePage = 1, isPracticeMode = 0;
		try {
			ResultSet rs = stmt.executeQuery("SELECT creatorUserName, quizName, description, category, isRandom, isOnepage, isPracticeMode FROM quiz WHERE quizID = \"" + quizID + "\";");
			if(rs.next()){				
				creatorName = rs.getString(1);
				quizName = rs.getString(2);
				description = rs.getString(3);
				category = rs.getString(4);
				isRandom = Integer.parseInt(rs.getString(5));
				isOnePage = Integer.parseInt(rs.getString(6));
				isPracticeMode = Integer.parseInt(rs.getString(7));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Quiz q = new Quiz(true, quizName, description, creatorName, category, isRandom, isOnePage, isPracticeMode);
		return q;
	}
	
	public static Quiz getQuizUsingID(Integer quizID, Statement stmt){
		if(quizID == 0) return null;
		String creatorName = "";
		String quizName = "", description = "", category = "";
		int isRandom = 0, isOnePage = 1, isPracticeMode = 0;
		try {
			ResultSet rs = stmt.executeQuery("SELECT creatorUserName, quizName, description, category, isRandom, isOnepage, isPracticeMode FROM quiz WHERE quizID = \"" + quizID + "\";");
			if(rs.next()){				
				creatorName = rs.getString(1);
				quizName = rs.getString(2);
				description = rs.getString(3);
				category = rs.getString(4);
				isRandom = Integer.parseInt(rs.getString(5));
				isOnePage = Integer.parseInt(rs.getString(6));
				isPracticeMode = Integer.parseInt(rs.getString(7));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Quiz q = new Quiz(true, quizName, description, creatorName, category, isRandom, isOnePage, isPracticeMode);
		return q;
	}
	
	public String getCreatorName(){
		return creatorName;
	}

	public String getName(){
		return quizName;
	}
	
	public String getDescription(){
		return description;
	}
	// to be used as q.getQuestion // player mode
	public static ArrayList<Question> getQuesListUsingID(int quizID, Statement stmt){
		ArrayList<Question> qList = new ArrayList<Question>();

		try {
			ResultSet rs = stmt.executeQuery("SELECT quesID, quesType, quesText, isOrdered FROM ques WHERE quizID = \"" + quizID + "\";");
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
	
	
	public static ArrayList<Question> getQuesListUsingID(Integer quizID, Statement stmt){
		ArrayList<Question> qList = new ArrayList<Question>();

		try {
			ResultSet rs = stmt.executeQuery("SELECT quesID, quesType, quesText, isOrdered FROM ques WHERE quizID = \"" + quizID + "\";");
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
	
	public static int getNumQuestionsUsingID(int quizID, Statement stmt){
		int result = 0;
		try {
			ResultSet rs = stmt.executeQuery("SELECT count(quesID) FROM ques WHERE quizID = \"" + quizID + "\";");
			if(rs != null){
				while(rs.next()){
					result = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
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
	
	public boolean isRandomVal(){
		return isRandom == 1;
	}
	
	public boolean isOnePage() {
		return isOnePage == 1;
	}
	
	public boolean hasPracticeMode(){
		return isPracticeMode == 1;
	}
	
	public void deleteQuizByCreatorName(String creatorName, Statement stmt){
		try {
			stmt.executeUpdate("DELETE FROM quiz WHERE creatorUserName = \"" + creatorName + "\";");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteQuizByQuizID(int quizID, Statement stmt){
		try {
			stmt.executeUpdate("DELETE FROM quiz WHERE quizID = \"" + quizID + "\";");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isTimedQuiz() {
		return true;
	}
	
	public static ArrayList<Integer> getQuizIDByTag(String tag, Statement stmt){
		ArrayList<Integer> quizIDList = new ArrayList<Integer>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT quizID FROM allTags WHERE tag = \"" + tag + "\";");
			while(rs.next()){
				quizIDList.add(Integer.parseInt(rs.getString(1)));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizIDList;
	}
	
	public static ArrayList<Integer> getTopQuiz(Statement stmt) {
		ArrayList<Integer> quizzes = new ArrayList<Integer>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT quizID from review GROUP BY quizID ORDER BY AVG(stars) LIMIT 10;");
			while(rs.next()){
				int quizID = rs.getInt("quizID");
				quizzes.add(quizID);
				}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizzes;
	}
	
	public static ArrayList<String> getTagsByQuizID(int quizID, Statement stmt){
		ArrayList<String> tags = new ArrayList<String>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT tag FROM allTags WHERE quizID = \"" + quizID + "\";");
			while(rs.next()){
				String oneTag = rs.getString(1);
				tags.add(oneTag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tags;
	}
}
