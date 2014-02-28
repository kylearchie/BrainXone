package brainxone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	 private static String account = MyDBInfo.MYSQL_USERNAME; 
	 private static String password = MyDBInfo.MYSQL_PASSWORD; 
	 private static String server = MyDBInfo.MYSQL_DATABASE_SERVER; 
	 private static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	 private Connection con;
	 private Statement stmt;
	 
	 public DBConnection() {
		 try { 
			 Class.forName("com.mysql.jdbc.Driver"); 
			 con = DriverManager.getConnection( "jdbc:mysql://" + server, account ,password); 
			 stmt = con.createStatement(); 
			 stmt.executeQuery("USE " + database); 
		 } catch (SQLException e) {  
			 e.printStackTrace(); 
		 } 
		 catch (ClassNotFoundException e) { 
			 e.printStackTrace(); 
		 } 
	 }
	 
	 public Statement getStatement() {
		 return stmt;
	 }
	 
	 public void destroy() {
		 if (con != null) {
			 try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 }
	 }
}

