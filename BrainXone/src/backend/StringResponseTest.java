package backend;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import brainxone.DBConnection;


public class StringResponseTest {

	@Test
	public void test1isOrdered(){
		DBConnection con = new DBConnection();
		Statement stmt = con.getStatement();
		StringResponse sr = new StringResponse(true, -1 , 8, "test1");
		
		try {
			stmt.executeUpdate("INSERT INTO ques VALUES (\"" + sr.getID() +"\",\"" + (-1) + "\",\"" + sr.getType()  +"\",\"" + sr.getQuesText() +"\",\"" + 0 +"\",\"" + 3 +"\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ArrayList<ArrayList<String>> answerKeys = new ArrayList<ArrayList<String>>();
		ArrayList<String> key1 = new ArrayList<String>();
		key1.add("dc");
		key1.add("washington");
		answerKeys.add(key1);
		ArrayList<String> key2 = new ArrayList<String>();
		key2.add("a");
		key2.add("b");
		answerKeys.add(key2);
		ArrayList<String> key3 = new ArrayList<String>();
		key3.add("1");
		answerKeys.add(key3);
		
		//sr.setAnswer(answerKeys, 3, stmt);
		
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("dc");
		ans.add("1");
		ans.add("1");
		System.out.println(sr.checkAnswer(ans, stmt));
		
	}
}
