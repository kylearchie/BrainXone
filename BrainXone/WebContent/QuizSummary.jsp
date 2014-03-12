<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.sql.*, backend.*, java.util.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
<title>Quiz Summary!</title>
</head>
<body>
<%@ include file="header.html" %>
<%
	int quizID = Integer.parseInt(request.getParameter("id"));
	Quiz q = Quiz.getQuizUsingID(quizID);
	out.print("Quiz Description: " + q.getDescription() + "<br>");
	out.print("Creator Name: " + q.getCreatorName() + "<br>");
	out.print("List of User's Past Performance: <br>");
	out.print("List of Highest Performance of All Time: <br>");
	out.print("List of Highest Performance in the Last Day: <br>");
	out.print("List of Performance of Recent Test Takers: <br>");
	out.print("Summary of staticstics of how well users have performed on quiz: <br> <br><br>");
	out.print("<li><b><a href=\"ShowQuiz.jsp?id=" + quizID + "\"> PLAY QUIZ </a></li>");
	if(q.hasPracticeMode()){
	%>
	
	<form action="PracticeModeServlet" method = "post">
	<input type = "submit" value = "Practice Mode">
	<input type = "hidden" name = "quizID" value = '<%= request.getParameter("id") %>' >
	</form>
	
	<%
		out.print("<li><a href=\"PracticeMode.jsp?id=" + quizID + "\"> PLAY IN PRACTICE MODE </a></li>");
	}
	out.print("<li><a href=\"EDITXXXXXXX.jsp?id=" + quizID + "\"> EDIT QUIZ </a> </b></li>");

%>
</body>
</html>