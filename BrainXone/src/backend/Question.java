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

	public static void init() {
		INSTRUCTIONS.put(0, "This is a standard text question with an appropriate text response. This question demands a SINGLE answer input, though there could be more than one right answer. This can be input on the next page.");
		INSTRUCTIONS.put(1, "Allow the user to select from one of a number of possible provided answers.");
		INSTRUCTIONS.put(2, "This is similar to standard Question-Response, except a blank can go anywhere within a question. Type '#BLANK' to signify a blank space.");
		INSTRUCTIONS.put(3, "This is similar to a standard multiple choice question, except the user can select more than one response.");
		INSTRUCTIONS.put(4, "In a picture response question, the system will display an image, and the user will provide a text response to the image. Type question, provide URL for related image.");
		INSTRUCTIONS.put(6, "This is a standard text question with an appropriate text response. This question demands a SINGLE answer input, though there could be more than one right answer. This can be input on the next page.");
		INSTRUCTIONS.put(8, "This is similar to the standard question-response, except there needs to be more than one text field for responses. You get to determine if the responses need to be in a particular order or not.");
		INSTRUCTIONS.put(9, "Matching");
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

	public String getParsedQuesText( int counter ){
		String result = "<span>";
		String questionParsing = quesText;
		int quesIndex = quesText.indexOf("#QTEXT");
		if( quesIndex != -1 ) {
			questionParsing = questionParsing.substring(quesIndex + "#QTEXT".length() + 1);
		}
		int imgIndex = questionParsing.indexOf("#IMGURL");
		if( imgIndex != -1 ) {
			result += questionParsing.substring(0, imgIndex) + "</span>";
			result += "<img src='" + questionParsing.substring(imgIndex + 1) + "'>";
		} else if( counter != -1) {
			int blankIndex = questionParsing.indexOf("#BLANK");
			if( blankIndex != -1 ) {
				result += questionParsing.substring(0, blankIndex) + "</span>";
				result += "<input type='text' name='question" + counter + "answer1'>";
				if(blankIndex + "#BLANK".length() + 1 < questionParsing.length()) {
					result += questionParsing.substring(blankIndex + "#BLANK".length() + 1);
				}
			} else {
				result += questionParsing + "</span><div class='quiz-answers'>";
				result += "<input type='text' name='question" + counter + "answer1'></div>";
			}
		} else {
			result += questionParsing;
		}
		return result;
	}
	
	public String getParsedQuesText(){
		return getParsedQuesText( -1 );
	}
	
	public String getQuesText() {
		return quesText;
	}

	public static void initMinID(int oldID){
		ID = oldID;
	}

	public int getQuesType(){
		return quesType;
	}

	public int checkAnswer(ArrayList<String> answers, Statement stmt){
		return points;
	}

	public int checkAnswer(HashSet<String> mapB, Statement stmt){
		return points;
	}

	public HashMap<String, Integer> displayAnswers(Statement stmt){
		return null;
	}

	public int getMaxPoints(Statement stmt) {
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
