<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
     <%@ page import = "java.sql.*, brainxone.*, java.util.*, backend.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Quiz Search Results</title>
</head>
<body>
<%
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
String userName = (String) session.getAttribute("currentUser");
String searchTerm = (String) session.getAttribute("tag");

ArrayList<Integer> quizes = Quiz.getQuizIDByTag(searchTerm, stmt);
System.out.println("****" + quizes.size());
out.println("<ul>");
for (Integer id : quizes) 
{   
	String name = null;
	try {
		ResultSet rs = stmt.executeQuery("SELECT quizName FROM quiz WHERE quizID = " + id +";");
		while (rs.next()) {
	    	name = rs.getString("quizName");    	
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	    	
	String quiz = "<a href = \"QuizSummary.jsp?id=" + id + "\"> " + id  + "</a>";	
	out.println("<li> QUIZ: " + quiz + "</li>");
}
out.println("</ul>");

%>
</body>
</html>