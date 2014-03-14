package backend;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import brainxone.DBConnection;


public class MultiChoiceTest {
	@Test
	public void test1isOrdered(){
		DBConnection con = new DBConnection();
		Statement stmt = con.getStatement();
		MultiChoice mc = new MultiChoice(true, -2 , 3, "test2");
		
		try {
			stmt.executeUpdate("INSERT INTO ques VALUES (\"" + mc.getID() +"\",\"" + (-1) + "\",\"" + mc.getType()  +"\",\"" + mc.getQuesText() +"\",\"" + 0 +"\",\"" + 1 +"\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		HashMap<String, Integer> answerKeys = new HashMap<String, Integer>();
		answerKeys.put("hi", 1);
		answerKeys.put("H", 1);
		
		mc.setAnswer(answerKeys, stmt);
		
		HashSet<String> ans = new HashSet<String>();
		ans.add("H");
		ans.add("hi");
		System.out.println(mc.checkAnswer(ans, stmt));
		
	}
}
