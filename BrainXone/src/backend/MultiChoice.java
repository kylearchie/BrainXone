package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class MultiChoice extends Question{		

	public MultiChoice(boolean isPlayerMode, int quesID, int quesType, String quesText) {
		super(isPlayerMode, quesID, quesType, quesText);
	}

	/** 
	 * CREATOR MODE
	 * 
	 */
	private void setAnswer(HashMap<String, Boolean> answerKeys) {
		maxPoints = 0;
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			for(Map.Entry<String, Boolean> ans : answerKeys.entrySet()){
				String option = ans.getKey();
				boolean isValid = ans.getValue();
				if(isValid == true) maxPoints++;
				stmt.executeUpdate("INSERT INTO ansOptions VALUES (\"" + ID +"\",\"" + option + "\",\"" + isValid + "\");");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


	public int getMaxPoints(){
		return maxPoints;
	}

	/**
	 * PLAYER MODE
	 * @param quesID
	 * @param mapB
	 * @return
	 */
	@Override
	public void checkAnswer(int quesID, HashSet<String> mapB){
		points = 0;
		HashMap<String, Boolean> mapA = new HashMap<String, Boolean>();
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			ResultSet rs = stmt.executeQuery("SELECT ansOption, isValid FROM ansOptions WHERE quesID = " + quesID);
			while(rs.next()){
				mapA.put(rs.getString(1), Boolean.parseBoolean(rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for(String ans : mapB){
			if(mapA.containsKey(ans)) {
				points++;
			}
		}
	}

}
