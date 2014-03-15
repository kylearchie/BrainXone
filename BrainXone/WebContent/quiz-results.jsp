<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
     <%@ page import = "java.sql.*, brainxone.*, java.util.*, backend.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Quiz Search Results</title>
</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">

		<h1 class="page-title">Quiz Search Results</h1>
<%
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
String userName = (String) session.getAttribute("currentUser");
//String searchTerm = (String) session.getAttribute("tag");
String searchTerm = request.getParameter("tag");

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
		e.printStackTrace();
	}	    	
	String quiz = "<a href = \"QuizSummary.jsp?id=" + id + "\"> " +  Quiz.getName(id, stmt)  + "</a>";	
	out.println("<li>" + quiz + "</li>");
}
out.println("</ul>");

%>
</div></div>
</body>
</html>