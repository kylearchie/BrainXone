package brainxone;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Account {
	private String userName;
	private String password;
	
	public Account(String userName, String password, Statement stmt){
		this.userName = userName;
		MessageDigest mdigest;
		try {
			mdigest = MessageDigest.getInstance("SHA");
			byte[] inputBytes = password.getBytes();
			byte[] outputBytes = mdigest.digest(inputBytes);
			this.password = hexToString(outputBytes);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		try {
			stmt.executeUpdate("INSERT INTO accounts VALUES(\"" + this.userName + "\",\"" + this.password  + "\");");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	public boolean accountExist(String account, Statement stmt){
		ResultSet rs;
		int size = 0;
		try {
			rs = stmt.executeQuery("SELECT * FROM accounts WHERE userName = \"" + account + "\";");			
			while (rs.next()) {
		    	size++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return size == 1;
	}
	
	public boolean passwordMatch(String account, String password, Statement stmt) {
		ResultSet rs;
		int size = 0;
		MessageDigest mdigest;
		String attemptedPassword = null;
		try {
			mdigest = MessageDigest.getInstance("SHA");
			byte[] inputBytes = password.getBytes();
			byte[] outputBytes = mdigest.digest(inputBytes);
			attemptedPassword = hexToString(outputBytes);			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		String realPassword = null;
		try {
			rs = stmt.executeQuery("SELECT password FROM accounts WHERE userName = \"" + account + "\";");			
			while (rs.next()) {
		    	realPassword = rs.getString("password");
				size++;
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		return size == 1 && attemptedPassword.equals(realPassword);
	}
	
//	public void createNewAccount(String account, String password) {
//		map.put(account, password);
//	}
}
