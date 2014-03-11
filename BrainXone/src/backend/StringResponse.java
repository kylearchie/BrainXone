package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class StringResponse extends Question{
	public StringResponse(boolean isPlayerMode, int quesID, int quesType, String quesText) {
		super(isPlayerMode, quesID, quesType, quesText);
	}

	/**
	 * For the PLAYER MODE
	 * Simply populates points that the player should get based
	 * on the number of correct answers she has 
	 * @param quesID
	 * @param mapB
	 * @return
	 */
	@Override
	public void checkAnswer(int quesID, HashMap<String, Integer> mapB){
		points = 0;
		int isOrdered = -1;
		HashMap<String, Integer> mapA = new HashMap<String, Integer>();
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();

		try {
			ResultSet rs1 = stmt.executeQuery("SELECT isOrdered FROM ques WHERE quesID = " + quesID);
			if(rs1.next()){
				isOrdered = Integer.parseInt(rs1.getString(1));
			}	

			ResultSet rsx = stmt.executeQuery("SELECT ans, ansIndex FROM indexAnswer WHERE quesID = " + quesID);

			while(rsx.next()){
				mapA.put(rsx.getString(1), Integer.parseInt(rsx.getString(2)));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for(String ans : mapB.keySet()){
			if(isOrdered == 1) {
				// matches
				if(mapA.containsKey(ans) && (mapA.get(ans) == mapB.get(ans))) {
					points++;
					mapA.remove(ans);
				}
			} else if(isOrdered == 0) {
				// matches
				if(mapA.containsKey(ans)) {
					points++;
					mapA.remove(ans);
				}
			}
		}
	}

	/**
	 * For the CREATOR MODE
	 * To be ideally used when the creator hits "create quiz"
	 * Populates answer given by creator and calls this function
	 * to SET the answers into the indexedAnswerDB
	 * @param answerKeys
	 */
	public void setAnswer(HashMap<String, Integer> answerKeys){
		maxPoints = answerKeys.size();
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		for(String ans : answerKeys.keySet()){
			try {
				stmt.executeUpdate("INSERT INTO indexAnswer VALUES (\"" + quesID +"\",\"" + ans + "\",\"" + answerKeys.get(ans) + "\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
