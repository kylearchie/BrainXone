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
		HashMap<Integer, ArrayList<String>> answerKeys = new HashMap<Integer, ArrayList<String>>();

		try {
			ResultSet rs = stmt.executeQuery("SELECT isOrdered FROM ques WHERE quesID = " + quesID);
			if(rs.next()){
				isOrdered = Integer.parseInt(rs.getString(1));
			}	
			rs = stmt.executeQuery("SELECT ans, ansIndex FROM indexAnswer WHERE quesID = " + quesID);
			while(rs.next()){
				String key = rs.getString(1);
				int index = Integer.parseInt(rs.getString(2));
				ArrayList<String> newKeys;
				if(answerKeys.containsKey(index))
					newKeys = answerKeys.get(index);
				else
					newKeys = new ArrayList<String>();
				newKeys.add(key);
				answerKeys.put(index, newKeys);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("answers " + answers);
		System.out.println("Keys " + answerKeys);
		
		int keysCount = answerKeys.size();
		for(int i = 0; i < answers.size(); i++){
			String oneAns = answers.get(i);
			for(int j = 1; j <= keysCount; j++){
				if(answerKeys.containsKey(j)){
					for(String key : answerKeys.get(j)){
						if(key.equals(oneAns)){
							if(isOrdered == 0 ){
								answerKeys.remove(j);
								points++;
								System.out.println("points: " + points);
								break;
							}
							else if(isOrdered == 1){
								if((i + 1) == j){
									answerKeys.remove(j);
									points++;
									System.out.println("points: " + points);
									break;
								}
							}
						}
					}
					}
				}
			}
		return points;
	}

	public HashMap<Integer, ArrayList<String>> getAnswerKeys(Statement stmt){
		
		HashMap<Integer, ArrayList<String>> answerKeys = new HashMap<Integer, ArrayList<String>>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT ans, ansIndex FROM indexAnswer WHERE quesID = " + quesID);
			while(rs.next()){
				String key = rs.getString(1);
				int index = Integer.parseInt(rs.getString(2));
				ArrayList<String> newKeys;
				if(answerKeys.containsKey(index))
					newKeys = answerKeys.get(index);
				else
					newKeys = new ArrayList<String>();
				newKeys.add(key);
				answerKeys.put(index, newKeys);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return answerKeys;
	}
	/**
	 * For the CREATOR MODE
	 * To be ideally used when the creator hits "create quiz"
	 * Populates answer given by creator and calls this function
	 * to SET the answers into the indexedAnswerDB
	 * @param answerKeys
	 */
	public void setAnswer(ArrayList<ArrayList<String>> answerKeys, int maxPoints, Statement stmt){
		this.maxPoints = maxPoints;
		
		System.out.println(answerKeys);
		// index starting from 1
		for(int i = 0; i < answerKeys.size(); i++){
			try {
				for(String key : answerKeys.get(i)){
					String sql = "INSERT INTO indexAnswer VALUES (\"" + quesID +"\",\"" + key + "\",\"" + (i + 1) + "\");";
					System.out.println(sql);
					stmt.executeUpdate(sql);
				}	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
