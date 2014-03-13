package backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	public int checkAnswer(ArrayList<String> answers, Statement stmt){
		points = 0;
		int isOrdered = -1;
		HashMap<Integer, String> answerKeys = new HashMap<Integer, String>();

		try {
			ResultSet rs = stmt.executeQuery("SELECT isOrdered FROM ques WHERE quesID = " + quesID);
			if(rs.next()){
				isOrdered = Integer.parseInt(rs.getString(1));
			}	
			rs = stmt.executeQuery("SELECT ans, ansIndex FROM indexAnswer WHERE quesID = " + quesID);
			while(rs.next()){
				answerKeys.put(Integer.parseInt(rs.getString(2)), rs.getString(1));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("answers " + answers);
		System.out.println("Keys " + answerKeys);
		
		int keysCount = answerKeys.size();
		for(int i = 0; i < answers.size(); i++){
			String oneAns = answers.get(i);
			if(!answerKeys.values().contains(oneAns))
				continue;
			for(int j = 1; j <= keysCount; j++){
				if(answerKeys.containsKey(j) && answerKeys.get(j).equals(oneAns)){
					if(isOrdered == 0 ){
						answerKeys.remove(j);
						points++;
						System.out.print("points: " + points);
					}
					else if(isOrdered == 1){
						if((i + 1) == j){
							answerKeys.remove(j);
							points++;
							System.out.println("points: " + points);
						}
					}
					}
				}
			}
		return points;
	}

	/**
	 * For the CREATOR MODE
	 * To be ideally used when the creator hits "create quiz"
	 * Populates answer given by creator and calls this function
	 * to SET the answers into the indexedAnswerDB
	 * @param answerKeys
	 */
	public void setAnswer(ArrayList<String> answerKeys, int maxPoints, Statement stmt){
		this.maxPoints = maxPoints;
		
		// index starting from 1
		for(int i = 0; i < answerKeys.size(); i++){
			try {
				stmt.executeUpdate("INSERT INTO indexAnswer VALUES (\"" + quesID +"\",\"" + answerKeys.get(i) + "\",\"" + (i + 1) + "\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
