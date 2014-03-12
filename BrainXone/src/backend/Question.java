package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Question {
	
	protected static int ID = 0;
	protected boolean isTimed;
	protected long time;
	protected String quesText;
	protected int maxPoints;
	protected int points;
	protected int quesType;
	protected int quesID;
		
	// REFERENCE
	public static final int STRING_RESPONSE = 0;
	public static final int MULTI_CHOICE = 1;
	public static final int FIB = 2;
	public static final int PICTURE_RESPONSE = 4;
	public static final int SINGLE_STR_ANS = 6;
	public static final int MULTI_STR_ANS = 8;
	public static final int MULTI_CHOICE_R = 1;
	public static final int MULTI_CHOICE_C = 3;
	public static Map<Integer, String> INSTRUCTIONS = new HashMap<Integer, String>();
	
	public static void init(){
		INSTRUCTIONS.put(0, "Type question");
		INSTRUCTIONS.put(1, "1 Correct answer, multiple incorrect answers");
		INSTRUCTIONS.put(2, "Type '#BLANK' to signify a blank space.");
		INSTRUCTIONS.put(3, "n Correct answers, m incorrect answers");
		INSTRUCTIONS.put(4, "Type question, provide URL for related image.");
		INSTRUCTIONS.put(6, "Type single question, and on the next page, put in single str answer");
		INSTRUCTIONS.put(8, "Type question for which you anticipate multiple answers");
	}
	
	
	// note: add 8* sub categories all that lead to 0 or 1.
	public Question(boolean isPlayerMode, int quesID, int quesType, String quesText){
		if(!isPlayerMode){
			ID++;
			this.quesID = ID;
		}
		else 
			this.quesID = quesID;
		this.quesType = quesType;
		this.quesText = quesText;
	}
	
	public int getID(){
		return quesID;
	}
	
	public String getQuesText(){
		return quesText;
	}
	
	public static void initMinID(int oldID){
		ID = oldID;
	}
	
	public int getQuesType(){
		return quesType;
	}
	
	public int checkAnswer(ArrayList<String> answers){
		return points;
	}
	
	public int checkAnswer(HashSet<String> mapB){
		return points;
	}
	
	public HashMap<String, Integer> displayAnswers(){
		return null;
	}
	
	public int getMaxPoints() {
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT maxPoints FROM ques WHERE quesID = " + quesID);
			if(rs.next()){
				maxPoints = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxPoints;
	}
	
	public int getType(){
		return quesType;
	}
}
