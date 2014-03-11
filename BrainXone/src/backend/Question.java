package backend;

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
		INSTRUCTIONS.put(0, "SINGLE ANSWER");
		INSTRUCTIONS.put(1, "MULTI ANSWER");
	}
	
	
	// note: add 8* sub categories all that lead to 0 or 1.
	public Question(boolean isPlayerMode, int quesID, int quesType, String quesText){
		if(!isPlayerMode) 
			ID++;
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
	
	public void checkAnswer(int quesID, HashMap<String, Integer> mapB){
	}
	
	public void checkAnswer(HashSet<String> mapB){
	}
	
	public int getPoints() {
		return points;
	}
	
	public HashMap<String, Integer> displayAnswers(){
		return null;
	}
	
	public int getMaxPoints() {
		return maxPoints;
	}
}
