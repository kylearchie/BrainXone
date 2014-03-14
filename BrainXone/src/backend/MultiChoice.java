package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletContext;

class MultiChoice extends Question{		

	public MultiChoice(boolean isPlayerMode, int quesID, int quesType, String quesText) {
		super(isPlayerMode, quesID, quesType, quesText);
	}

	/** 
	 * CREATOR MODE
	 * 
	 */
	public int setAnswer(HashMap<String, Integer> answerKeys, Statement stmt) {
		maxPoints = 0;
		try {
			for(Map.Entry<String, Integer> ans : answerKeys.entrySet()){
				String option = ans.getKey();
				int isValid = ans.getValue();
				if(isValid == 1) maxPoints++;
				stmt.executeUpdate("INSERT INTO ansOptions VALUES (\"" + quesID +"\",\"" + option + "\",\"" + isValid + "\");");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return maxPoints;
	}

	/**
	 * PLAYER MODE
	 * @param quesID
	 * @param mapB
	 * @return
	 */
	@Override
	public int checkAnswer(HashSet<String> mapB, Statement stmt){
		points = 0;
		HashMap<String, Integer> mapA = displayAnswers(stmt);
		System.out.println(mapA);
		for(String ans : mapB){
			if(mapA.containsKey(ans) && mapA.get(ans) == 1) {
				points++;
			}
		}
		return points;
	}
	
	/** 
	 * Function to display the MCQs when we retrieve a question
	 * Populates & returns a map with answers from database
	 * @return
	 */
	@Override
	public HashMap<String, Integer> displayAnswers(Statement stmt){
		HashMap<String, Integer> mapA = new HashMap<String, Integer>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT ansOption, isVaid FROM ansOptions WHERE quesID = " + quesID);
			while(rs.next()){
				mapA.put(rs.getString(1), Integer.parseInt(rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapA;
	}

}
